resource "aws_ecs_cluster" "springboot" {
  name = "${var.app_name}-cluster"
}
