#####################################
# Security Group Settings
#####################################
resource "aws_security_group" "public_firewall" {
    name = "${var.app_name} public-firewall"
    vpc_id = "${aws_vpc.vpc.id}"
    ingress {
        from_port = 0
        to_port = 0
        protocol = "-1"
        cidr_blocks = ["${var.root_segment}"]
    }
    ingress {
        from_port = 0
        to_port = 0
        protocol = "-1"
        cidr_blocks = ["${var.myip}"]
    }
    egress {
        from_port = 0
        to_port = 0
        protocol = "-1"
        cidr_blocks = ["0.0.0.0/0"]
    }
    tags {
        Name = "${var.app_name} public-firewall"
        Group = "${var.app_name}"
    }
    description = "${var.app_name} public-firewall"
}

resource "aws_security_group" "private_firewall" {
    name = "${var.app_name} private-firewall"
    vpc_id = "${aws_vpc.vpc.id}"
    ingress {
        from_port = 0
        to_port = 0
        protocol = "-1"
        cidr_blocks = ["${var.root_segment}"]
    }
    egress {
        from_port = 0
        to_port = 0
        protocol = "-1"
        cidr_blocks = ["0.0.0.0/0"]
    }
    tags {
        Name = "${var.app_name} private-firewall"
        Group = "${var.app_name}"
    }
    description = "${var.app_name} private-firewall"
}