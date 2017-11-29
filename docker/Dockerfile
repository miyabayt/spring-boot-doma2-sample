FROM centos:centos6

RUN yum clean all
RUN yum -y update

# epel install
RUN rpm -ivh http://downloads.naulinux.ru/pub/SLCE/6x/x86_64/CyrEd/RPMS/epel-release-6-8.1.noarch.rpm

RUN yum -y install xz zip unzip wget
RUN yum -y install openssh-server openssh-clients passwd
RUN yum -y install mysql mysql-server
RUN yum -y install supervisor --enablerepo=epel

RUN sed -ri 's/^#PermitEmptyPasswords no/PermitEmptyPasswords yes/' /etc/ssh/sshd_config
RUN sed -ri 's/^#PermitRootLogin yes/PermitRootLogin yes/' /etc/ssh/sshd_config
RUN sed -ri 's/^UsePAM yes/UsePAM no/' /etc/ssh/sshd_config

RUN passwd -d root
RUN ssh-keygen -t rsa -N "" -f /etc/ssh/ssh_host_rsa_key

# mysql init
RUN /etc/init.d/mysqld start \
  && mysqladmin -u root password '' \
  && (echo 'grant all privileges on *.* to root@"%" identified by "passw0rd" with grant option;' | mysql -u root) \
  && (echo 'create database sample character set utf8;' | mysql -u root) \
  && /etc/init.d/mysqld stop

RUN /etc/init.d/mysqld start \
  && mysqladmin -u root password 'passw0rd' \
  && /etc/init.d/mysqld stop

COPY ./my.cnf /root/
RUN /bin/cp -f /root/my.cnf /etc/
RUN mkdir -p /var/lib/mysql/tmp
RUN chown mysql.mysql /var/lib/mysql/tmp
RUN mkdir -p /var/log/mysql
RUN chown mysql.mysql /var/log/mysql

# supervisordの設定
RUN echo '[include]' >> /etc/supervisord.conf
RUN echo 'files = supervisord/conf/*.conf' >> /etc/supervisord.conf
RUN mkdir -p  /etc/supervisord/conf/

RUN echo '[supervisord]'  >> /etc/supervisord.conf
RUN echo 'nodaemon=true'  >> /etc/supervisord.conf
RUN echo '[program:sshd]' >> /etc/supervisord.conf
RUN echo 'command=/usr/sbin/sshd -D'  >> /etc/supervisord.conf
RUN echo '[program:mysqld]' >> /etc/supervisord.conf
RUN echo 'command=/usr/bin/mysqld_safe' >> /etc/supervisord.conf

RUN yum clean all

EXPOSE 22 3306

# run
CMD ["/usr/bin/supervisord", "-n"]
