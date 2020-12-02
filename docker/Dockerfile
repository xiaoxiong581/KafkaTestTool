FROM openjdk:8

MAINTAINER zhixiong15172@163.com

EXPOSE 29080

ADD KafkaTestTool-1.0.0-snapshot.tar.gz /opt
ADD entrypoint.sh /opt

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone
RUN rm -f /opt/KafkaTestTool/lib/slf4j-log4j12-1.7.26.jar
#RUN find /opt -name "*.sh" | xargs dos2unix

ENTRYPOINT ["/opt/entrypoint.sh"]