#####################################
# ALB Setting
#####################################
resource "aws_alb" "springboot" {
  name = "${var.app_name}-alb"
  internal = false

  security_groups = ["${data.aws_security_group.public.id}"]
  subnets = ["${data.aws_subnet.public_1.id}","${data.aws_subnet.public_2.id}"]

  access_logs {
    bucket = "${var.app_name}-accesslog"
    prefix = "alb_log"
  }

  idle_timeout = 400

  tags {
      Name = "${var.app_name}-alb"
      Group = "${var.app_name}"
  }
}

resource "aws_alb_target_group" "springboot" {
  name     = "${var.app_name}-tg"
  port     = 80
  protocol = "HTTP"
  vpc_id   = "${data.aws_vpc.selected.id}"
  target_type = "ip"

  tags {
      Name = "${var.app_name}-tg"
      Group = "${var.app_name}"
  }
}

resource "aws_alb_listener" "springboot" {
  load_balancer_arn = "${aws_alb.springboot.id}"
  port              = "80"
  protocol          = "HTTP"

  default_action {
    target_group_arn = "${aws_alb_target_group.springboot.id}"
    type             = "forward"
  }
}