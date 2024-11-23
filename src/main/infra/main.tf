terraform {
  backend "s3" {
    bucket = "project24-tf-state"
    key    = "proejct24-state"
    region = "eu-west-1"
  }
}

provider "aws" {
  region = var.aws_region

  default_tags {
    tags = {
      Name = "project24-api"
    }
  }
}

data "aws_default_tags" "default" {}

locals {
  name = data.aws_default_tags.default.tags["Name"]
}