resource "aws_db_subnet_group" "aurora_subnet_group" {
    name          = "${var.app_name}-aurora-db-subnet-group"
    subnet_ids    = [
        "${data.aws_subnet.public_1.id}", "${data.aws_subnet.public_2.id}", "${data.aws_subnet.public_3.id}"
    ]

    tags {
        Name         = "${var.app_name}-aurora-db-subnet-group"
        VPC          = "${data.aws_vpc.selected.id}"
    }
}

resource "aws_rds_cluster_parameter_group" "default" {
  name        = "rds-cluster-pg"
  family      = "aurora5.6"

  parameter {
    name  = "character_set_server"
    value = "utf8"
  }

  parameter {
    name  = "character_set_client"
    value = "utf8"
  }
}

resource "aws_rds_cluster" "default" {
  cluster_identifier = "${var.app_name}-aurora-cluster"
  availability_zones = ["${var.az1}", "${var.az2}", "${var.az3}"]
  db_subnet_group_name  = "${aws_db_subnet_group.aurora_subnet_group.name}"
  vpc_security_group_ids = ["${data.aws_security_group.private.id}"]
  db_cluster_parameter_group_name = "${aws_rds_cluster_parameter_group.default.name}"
  database_name      = "sample"
  master_username    = "root"
  master_password    = "passw0rd"
  final_snapshot_identifier = "${var.app_name}-aurora-cluster-final"
  skip_final_snapshot       = true
}

resource "aws_rds_cluster_instance" "cluster_instances" {
  count              = 1
  identifier         = "${var.app_name}-aurora-cluster-${count.index}"
  cluster_identifier = "${aws_rds_cluster.default.id}"
  db_subnet_group_name = "${aws_db_subnet_group.aurora_subnet_group.name}"
  instance_class     = "${var.db_instance_type}"
  performance_insights_enabled = false
}

