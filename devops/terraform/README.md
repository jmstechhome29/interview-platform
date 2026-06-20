# Terraform — AWS infrastructure

```
devops/terraform/
  modules/
    vpc/    -> VPC, public+private subnets, NAT, route tables
    iam/    -> EKS cluster role, EKS node role, Jenkins CI role (ECR push)
    ecr/    -> one ECR repo per microservice, scan-on-push enabled
    eks/    -> EKS cluster + managed node group
    alb/    -> public ALB + target group + security group, in front of NGINX ingress
  envs/
    dev/    -> wires the modules together for the dev environment
```

## Usage

```bash
cd devops/terraform/envs/dev
cp terraform.tfvars.example terraform.tfvars   # fill in your values
terraform init
terraform plan  -var-file=terraform.tfvars
terraform apply -var-file=terraform.tfvars
```

After apply, point kubectl at the new cluster and install the NGINX ingress
controller + ArgoCD (see ../../../docs/DEPLOYMENT_GUIDE.md):

```bash
aws eks update-kubeconfig --name interview-platform-dev --region us-east-1
```

Modules are intentionally reusable — copy `envs/dev` to `envs/staging` or
`envs/prod` and just change the tfvars/instance sizes.
