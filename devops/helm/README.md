# Helm charts — umbrella + library pattern

```
devops/helm/
  common/                  <- library chart: ONE copy of Deployment/Service/HPA/Ingress/ConfigMap/Secret templates
  charts/
    user-service/          <- ~10 line Chart.yaml + values.yaml that overrides image/port/env only
    resume-service/
    interview-service/
    feedback-service/
    api-gateway/
    frontend/
  umbrella/                <- aggregates all 6 charts, one `helm install` deploys everything
```

Why this shape: every service chart's `templates/all.yaml` is identical (it just
calls `common.deployment`, `common.service`, etc.) — only `values.yaml` differs
per service. That satisfies "don't repeat the same Helm chart, just override
minimal changes per service."

## Install everything

```bash
cd devops/helm
helm dependency update umbrella
helm install interview-platform umbrella -n interview-platform --create-namespace
```

## Install/upgrade a single service (e.g. after a Jenkins build bumped its tag)

```bash
helm upgrade interview-platform-user-service charts/user-service \
  -n interview-platform --set image.tag=42
```

## Rollback

```bash
helm history interview-platform -n interview-platform
helm rollback interview-platform <REVISION> -n interview-platform
```

In the GitOps flow (see ../argocd) ArgoCD does this automatically: it watches
the gitops repo, and `helm rollback` is replaced by `argocd app rollback`.
