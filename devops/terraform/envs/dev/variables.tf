variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "jenkins_trusted_principal_arn" {
  type        = string
  description = "ARN Jenkins assumes to push to ECR (IAM user or OIDC role)."
}
