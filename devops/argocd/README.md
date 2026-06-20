# ArgoCD GitOps

```bash
kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

kubectl apply -f project.yaml
kubectl apply -f application.yaml
```

Argo will then continuously sync `devops/helm/umbrella` into the
`interview-platform` namespace. Rollback:

```bash
argocd app history interview-platform
argocd app rollback interview-platform <REVISION>
```

Jenkins' "Update GitOps Repo" stage bumps the `image.tag` in the relevant
service's `values.yaml` under `devops/helm/charts/<service>/values.yaml` (or
an env overlay) and pushes — Argo's auto-sync picks it up within ~3 minutes,
or instantly if a webhook is configured.
