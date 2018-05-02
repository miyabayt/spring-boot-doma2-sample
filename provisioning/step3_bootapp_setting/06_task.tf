#####################################
# Task Setting
#####################################
data "template_file" "springboot" {
  template = "${file("task/app.json")}"

  vars {
    app_name           = "${var.app_name}"
    aws_region         = "${var.region}"
    aws_id             = "${var.aws_id}"
  }
}

resource "aws_ecs_task_definition" "springboot" {
  family = "springboot"
  container_definitions = "${data.template_file.springboot.rendered}"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  execution_role_arn       = "${aws_iam_role.ecs_task_role.arn}"
  cpu                      = 512
  memory                   = 1024

  depends_on = [
    "aws_cloudwatch_log_group.springboot"
  ]
}
