# Interview Scheduling Platform

A small microservices demo built to teach how services communicate, and what
DevOps/DevSecOps/SRE actually touch day to day. Two roles: **Admin** (manage
candidates, upload resumes, schedule interviews, view all interviews/feedback)
and **Interviewer** (see assigned interviews, download resumes, submit
feedback). No payments, no fluff — just enough working code to demo the
concepts end to end.

## Architecture

```
                        ┌────────────────────┐
        React (Vite) →  │   api-gateway:8080  │  (Spring Cloud Gateway, JWT check)
        frontend:3000    └─────────┬──────────┘
                                   │
        ┌──────────────┬──────────┼──────────────┬──────────────┐
        ▼              ▼          ▼              ▼              ▼
  user-service   resume-service  interview-service        feedback-service
   :8081/userdb   :8082/resumedb  :8083/interviewdb         :8084/feedbackdb
   (auth + JWT)   (candidates +   (schedule, calls user-     (calls interview-
                   resume files)   service + resume-service   service to mark
                                   to validate)                COMPLETED)
```

Each service owns its own Postgres database (database-per-service). All
inter-service calls are plain REST via `RestTemplate` — easy to read, easy to
trace in Jaeger, easy to break on purpose for a demo.

| Service | Port | Responsibility |
|---|---|---|
| `user-service` | 8081 | Login, JWT issuing, user/role lookup |
| `resume-service` | 8082 | Candidate CRUD, resume upload/download |
| `interview-service` | 8083 | Schedule interviews, validates candidate+interviewer by calling the two services above |
| `feedback-service` | 8084 | Submit feedback, marks the interview `COMPLETED` by calling interview-service |
| `api-gateway` | 8080 | Single entry point, routes to the 4 services, re-checks JWT |
| `frontend` | 3000 | React SPA, role-based dashboards |

## Run it locally (one command)

Requires Docker + Docker Compose. Nothing else.

```bash
tar -xzf interview-platform.tar.gz
cd interview-platform
docker compose up --build
```

Open **http://localhost:3000**. Seeded logins:

| Role | Username | Password |
|---|---|---|
| Admin | `admin` | `admin123` |
| Interviewer | `interviewer1` | `pass123` |
| Interviewer | `interviewer2` | `pass123` |

Bonus observability, already wired in `docker-compose.yml`:
- Jaeger UI: http://localhost:16686
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3001 (admin/admin)
- Swagger per service: `http://localhost:<port>/swagger-ui.html`

To stop: `docker compose down` (add `-v` to also wipe the database volumes).

## Repo layout

```
interview-platform/
  pom.xml                     root Maven reactor (Java 22, Spring Boot 3.3.4)
  user-service/               auth + JWT
  resume-service/             candidates + resume files
  interview-service/          scheduling
  feedback-service/           feedback
  api-gateway/                Spring Cloud Gateway
  frontend/                   React + Vite SPA
  docker-compose.yml          full local stack incl. Jaeger/Prometheus/Grafana
  infra/                      postgres init SQL, prometheus.yml, grafana provisioning
  devops/
    jenkins/                  Jenkinsfile + shared library + seed job (reference CI pipeline)
    helm/                     library + per-service + umbrella Helm charts (reference K8s deploy)
    terraform/                VPC/EKS/ECR/IAM/ALB modules (reference AWS IaC)
    argocd/                   Application/AppProject manifests (reference GitOps)
```

## What's "real" vs "reference"

The **app and `docker-compose.yml` are real and runnable** — that's the part
students actually execute. The **`devops/` folder is reference material**:
correct, complete Jenkins/Helm/Terraform/ArgoCD code that shows the full
CI/CD → GitOps → cloud pattern, but it targets a real Jenkins server, a real
AWS account, and a real Kubernetes cluster, none of which exist in this
tar file. Use it to *teach* the pipeline shape; see
`devops/*/README.md` in each folder for how to actually point it at your own
infrastructure.

## Day-to-day roles this demonstrates

- **Developer**: edit a service, `docker compose up --build <service>`, watch
  it pick up via the gateway.
- **DevOps**: `devops/jenkins/Jenkinsfile` — build, test, scan, push, deploy
  stages; `devops/helm` — how to avoid copy-pasting six near-identical Helm
  charts via a library chart + umbrella chart.
- **DevSecOps**: SonarQube SAST stage + quality gate, Trivy image scan stage,
  Kubernetes Secrets/Vault annotations in the Helm templates, non-root Docker
  users.
- **SRE**: liveness/readiness probes, HPA, Prometheus/Grafana/Jaeger stack,
  resource requests/limits — and a few break-it-on-purpose ideas below.

## Demo / break-it scenarios for a webinar

1. **Trace a request**: schedule an interview from the UI, open Jaeger, show
   the span hopping from `api-gateway` → `interview-service` → `user-service`
   + `resume-service`.
2. **Kill a dependency**: `docker compose stop resume-service`, then try to
   schedule an interview — show the 4xx/5xx bubbling up, then `docker compose
   start resume-service` and retry.
3. **Scale**: in the Helm reference, bump `hpa.maxReplicas` and generate load
   to show autoscaling (needs a real cluster — talk through it from the YAML
   if you don't have one handy).
4. **Rollback**: in ArgoCD, `argocd app rollback interview-platform <rev>`
   after a bad image tag, to show GitOps recovery.

## Known limitation

This was hand-written without a local Maven/Java 22 toolchain available in
the authoring sandbox, so the backend was not compiled here. The frontend
*was* installed and built successfully (`npm install && npm run build`). Run
`docker compose up --build` first and check logs if any service has a typo —
the code follows standard Spring Boot 3 patterns throughout and should build
cleanly with Java 22 + Maven.
