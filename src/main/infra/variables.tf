variable "aws_region" {
  description = "The AWS region to create things in."
  default     = "eu-west-1"
}

variable "az_count" {
  description = "Number of AZs to cover in a given AWS region"
  default     = "2"
}

# variable "admin_cidr_ingress" {
#   description = "CIDR to allow tcp/22 ingress to EC2 instance"
# }
