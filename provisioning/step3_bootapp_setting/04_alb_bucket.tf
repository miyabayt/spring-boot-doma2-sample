#####################################
# S3 Settings
#####################################
resource "aws_s3_bucket" "lb-log" {
    bucket = "${var.app_name}-accesslog"
    force_destroy = true

    tags {
        Name = "${var.app_name}-accesslog"
        Group = "${var.app_name}"
    }
}

data "aws_elb_service_account" "lb-log" {}

data "aws_iam_policy_document" "lb-log" {
    statement {
        actions = [
            "s3:PutObject",
        ]
        resources = [
            "arn:aws:s3:::${var.app_name}-accesslog/alb_log/AWSLogs/${var.aws_id}/*",
        ]
        "principals" = {
          type = "AWS"
          identifiers = [
            "${data.aws_elb_service_account.lb-log.id}",
          ]
        }
    }
}

resource "aws_s3_bucket_policy" "lb-log" {
  bucket = "${aws_s3_bucket.lb-log.id}"
  policy = "${data.aws_iam_policy_document.lb-log.json}"
}
