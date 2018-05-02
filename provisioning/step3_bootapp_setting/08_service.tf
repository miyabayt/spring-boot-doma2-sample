#####################################
# Service Setting
#####################################
resource "aws_ecs_service" "springboot" {
  name = "${var.app_name}-service"
  cluster = "${aws_ecs_cluster.springboot.id}"
  task_definition = "${aws_ecs_task_definition.springboot.arn}"
  desired_count = 1
  launch_type = "FARGATE"

  load_balancer {
    target_group_arn = "${aws_alb_target_group.springboot.id}"
    container_name = "springboot"
    container_port = 18081
  }

  network_configuration {
    subnets = [
      "${data.aws_subnet.public_1.id}",
      "${data.aws_subnet.public_2.id}"
    ]

    security_groups = [
      "${data.aws_security_group.public.id}"
    ]
    assign_public_ip = "true"
  }

  depends_on = [
    "aws_alb_listener.springboot"
  ]
}
