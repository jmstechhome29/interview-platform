# Deployment Guide

## 1. Local (what students actually run)

```bash
cd interview-platform
cp .env.example .env        # optional: change JWT secret / db creds
docker compose up --build
```

That's the whole local path. Services come up in this order (Compose
healthchecks handle the wait): postgres → 4 backend services → api-gateway →
frontend, plus jaeger/prometheus/grafana in parallel.

Troubleshooting:
- `docker compose logs -f <service>` to see why a service didn't become healthy.
- `docker compose down -v` to fully reset (wipes Postgres + uploaded resumes).

## 2. Reference cloud path (AWS + EKS) — not required to run the demo

This is the pattern the `devops/` folder encodes. To actually execute it you
need: an AWS account, a Jenkins server, and time.

1. **Provision infra** — `devops/terraform/envs/dev`:
   ```bash
   cd devops/terraform/envs/dev
   cp terraform.tfvars.example terraform.tfvars   # fill in real values
   terraform init && terraform apply -var-file=terraform.tfvars
   aws eks update-kubeconfig --name interview-platform-dev --region us-east-1
   ```
2. **Install cluster add-ons**:
   ```bash
   helm install ingress-nginx ingress-nginx/ingress-nginx -n ingress-nginx --create-namespace
   helm install vault hashicorp/vault -n vault --create-namespace   # if using Vault-injected secrets
   kubectl apply -f devops/argocd/   # ArgoCD app + project (after installing ArgoCD itself)
   ```
3. **CI/CD** — point a Jenkins instance at `devops/jenkins/Jenkinsfile`, seed
   per-service jobs with `devops/jenkins/seed-job/seed.groovy`, configure
   credentials for SonarQube, Nexus, and AWS ECR push. Each pipeline run:
   checkout → build/test → SonarQube SAST + quality gate → Docker build →
   Trivy scan → push to Nexus + ECR → bump the image tag in
   `devops/helm/charts/<service>/values.yaml` and push (gitops step).
4. **GitOps** — ArgoCD (already pointed at `devops/helm/umbrella` by
   `devops/argocd/application.yaml`) auto-syncs the new image tag into the
   cluster. Rollback with `argocd app rollback interview-platform <rev>`.
5. **Helm structure** — one `common` library chart holds every Kubernetes
   resource template once; each service chart (`devops/helm/charts/<svc>`)
   is just a `values.yaml` override; the `umbrella` chart aggregates all six
   with per-service `enabled` flags so `helm upgrade` deploys (or disables)
   the whole platform in one command. See `devops/helm/README.md`.

## 3. Observability

- **Jaeger** (`docker-compose.yml`, port 16686): every backend service is
  pre-wired with Micrometer tracing — open a trace for any request that hit
  the gateway to see the full call chain across services.
- **Prometheus + Grafana**: scrape configs in `infra/prometheus/prometheus.yml`,
  dashboards provisioned from `infra/grafana/provisioning/`. Each service
  exposes `/actuator/prometheus`.
- **Logs**: each service logs structured JSON (`logstash-logback-encoder`),
  ready to ship into an EFK stack (`docker compose logs` works fine for the
  local demo; ship to a real Elasticsearch with Filebeat/Fluentd in a real
  cluster).

## 4. Security touchpoints (DevSecOps)

- JWT issued by `user-service`, verified at both the gateway (defense in
  depth) and again inside each downstream service.
- `Jenkinsfile` includes a SonarQube SAST stage with a quality gate, and a
  Trivy scan stage that fails the build on critical/high CVEs in the image.
- Helm templates support HashiCorp Vault Agent injection
  (`vault.hashicorp.com/agent-inject` annotations) as an alternative to plain
  K8s Secrets for production secrets.
- Dockerfiles run the app as a non-root user in the final image stage.
