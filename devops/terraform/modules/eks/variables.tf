variable "cluster_name" { type = string }
variable "cluster_role_arn" { type = string }
variable "node_role_arn" { type = string }
variable "kubernetes_version" {
  type    = string
  default = "1.30"
}
variable "public_subnet_ids" { type = list(string) }
variable "private_subnet_ids" { type = list(string) }
variable "instance_types" {
  type    = list(string)
  default = ["t3.medium"]
}
variable "desired_size" {
  type    = number
  default = 2
}
variable "min_size" {
  type    = number
  default = 2
}
variable "max_size" {
  type    = number
  default = 5
}
