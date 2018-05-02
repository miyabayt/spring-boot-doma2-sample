#######################
# Required
#######################

variable "access_key" {}
variable "secret_key" {}
variable "aws_id" {}

#######################
# Option
#######################

variable "app_name" {
  default = "springboot-fargate-sample"
}

variable "region" {
  default = "us-east-1"
}

variable "az1" {
  default = "us-east-1a"
}

variable "az2" {
  default = "us-east-1b"
}

variable "az3" {
  default = "us-east-1c"
}

variable "root_segment" {
  default = "192.168.0.0/16"
}

variable "public_segment1" {
  default = "192.168.200.0/24"
}

variable "public_segment2" {
  default = "192.168.201.0/24"
}

variable "public_segment3" {
  default = "192.168.202.0/24"
}

variable "myip" {
  default = "153.156.43.75/32"
}

variable "db_instance_type" {
  default = "db.t2.small"
}