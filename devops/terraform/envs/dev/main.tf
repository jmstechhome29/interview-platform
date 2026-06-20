terraform {
  required_version = ">= 1.6"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
  # Recommended: move this to an S3 + DynamoDB remote backend for real use.
  # backend "s3" {
  #   bucket         = "interview-platform-tfstate"
  #   key            = "dev/terraform.tfstate"
  #   region         = "us-east-1"
  #   dynamodb_table = "interview-platform-tf-locks"
  # }
}

provider "aws" {
  region = var.aws_region
}

module "vpc" {
  source                = "../../modules/vpc"
  name                  = "interview-platform-dev"
  cluster_name          = "interview-platform-dev"
  azs                   = ["${var.aws_region}a", "${var.aws_region}b"]
  public_subnet_cidrs   = ["10.0.0.0/24", "10.0.1.0/24"]
  private_subnet_cidrs  = ["10.0.10.0/24", "10.0.11.0/24"]
}

module "iam" {
  source                         = "../../modules/iam"
  name                           = "interview-platform-dev"
  jenkins_trusted_principal_arn  = var.jenkins_trusted_principal_arn
}

module "ecr" {
  source = "../../modules/ecr"
}

module "eks" {
  source             = "../../modules/eks"
  cluster_name       = "interview-platform-dev"
  cluster_role_arn   = module.iam.eks_cluster_role_arn
  node_role_arn      = module.iam.eks_node_role_arn
  public_subnet_ids  = module.vpc.public_subnet_ids
  private_subnet_ids = module.vpc.private_subnet_ids
  desired_size       = 2
  min_size           = 2
  max_size           = 4
}

module "alb" {
  source            = "../../modules/alb"
  name              = "interview-platform-dev"
  vpc_id            = module.vpc.vpc_id
  public_subnet_ids = module.vpc.public_subnet_ids
}
