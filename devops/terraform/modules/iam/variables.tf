variable "name" { type = string }
variable "jenkins_trusted_principal_arn" {
  type        = string
  description = "ARN of the IAM user/role your Jenkins agent assumes (instance profile or OIDC role)."
}
