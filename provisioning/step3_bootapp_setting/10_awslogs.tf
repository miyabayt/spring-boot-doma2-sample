resource "aws_cloudwatch_log_group" "springboot" {
  name = "awslogs-${var.app_name}-log"
}
