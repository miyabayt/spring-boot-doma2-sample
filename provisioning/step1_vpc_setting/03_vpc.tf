#####################################
# VPC Settings
#####################################
resource "aws_vpc" "vpc" {
    cidr_block = "${var.root_segment}"
    tags {
        Name = "${var.app_name} vpc"
        Group = "${var.app_name}"
    }
}

#####################################
# Internet Gateway Settings
#####################################
resource "aws_internet_gateway" "igw" {
    vpc_id = "${aws_vpc.vpc.id}"
    tags {
        Name = "${var.app_name} igw"
        Group = "${var.app_name}"
    }
}

#####################################
# Public Subnets Settings
#####################################
resource "aws_subnet" "public-subnet1" {
    vpc_id = "${aws_vpc.vpc.id}"
    cidr_block = "${var.public_segment1}"
    availability_zone = "${var.az1}"
    map_public_ip_on_launch = true
    tags {
        Name = "${var.app_name} public-subnet1"
        Group = "${var.app_name}"
    }
}

resource "aws_subnet" "public-subnet2" {
    vpc_id = "${aws_vpc.vpc.id}"
    cidr_block = "${var.public_segment2}"
    availability_zone = "${var.az2}"
    map_public_ip_on_launch = true
    tags {
        Name = "${var.app_name} public-subnet2"
        Group = "${var.app_name}"
    }
}

resource "aws_subnet" "public-subnet3" {
    vpc_id = "${aws_vpc.vpc.id}"
    cidr_block = "${var.public_segment3}"
    availability_zone = "${var.az3}"
    map_public_ip_on_launch = true
    tags {
        Name = "${var.app_name} public-subnet3"
        Group = "${var.app_name}"
    }
}

#####################################
# Public Routes Table Settings
#####################################
resource "aws_route_table" "public-root-table" {
    vpc_id = "${aws_vpc.vpc.id}"
    route {
        cidr_block = "0.0.0.0/0"
        gateway_id = "${aws_internet_gateway.igw.id}"
    }
    tags {
        Name = "${var.app_name} public-root-table"
        Group = "${var.app_name}"
    }
}

resource "aws_route_table_association" "public-rta1" {
    subnet_id = "${aws_subnet.public-subnet1.id}"
    route_table_id = "${aws_route_table.public-root-table.id}"
}

resource "aws_route_table_association" "public-rta2" {
    subnet_id = "${aws_subnet.public-subnet2.id}"
    route_table_id = "${aws_route_table.public-root-table.id}"
}

resource "aws_route_table_association" "public-rta3" {
    subnet_id = "${aws_subnet.public-subnet3.id}"
    route_table_id = "${aws_route_table.public-root-table.id}"
}
