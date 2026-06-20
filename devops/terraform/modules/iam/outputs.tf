output "eks_cluster_role_arn" { value = aws_iam_role.eks_cluster_role.arn }
output "eks_node_role_arn" { value = aws_iam_role.eks_node_role.arn }
output "ci_cd_role_arn" { value = aws_iam_role.ci_cd_role.arn }
