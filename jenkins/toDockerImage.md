# java application
# 构建参数
NAME=appName
PROJECT=prjName
VERSION=0.1
REGISTRY=registry.xxx.com
USER=user1
PWD=password1

# 生产start.sh文件
cat > start.sh << EOF
#!/bin/sh
/usr/local/tomcat/bin/startup.sh
tail -f /usr/local/tomcat/logs/catalina.out
EOF

# 构建镜像
cat > Dockerfile << EOF
#FROM registry.xxx.com/template/tomcat8.5:0.1.0
FROM tomcat:8.5-jdk8-slim
ENV TZ Asia/Shanghai
#RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY target/xxx.war /usr/local/tomcat/webapps/xxx.war
COPY start.sh /usr/local/tomcat/bin/start.sh
RUN chmod +x /usr/local/tomcat/bin/start.sh
RUN mkdir -p /usr/local/tomcat/logs/xxx/track


CMD ["/usr/local/tomcat/bin/start.sh"]
EOF

docker build -t ${NAME}:${VERSION} .
# 登录仓库
docker login   -u ${USER} -p ${PWD}  ${REGISTRY}
#打TAG
docker tag ${NAME}:${VERSION}   ${REGISTRY}/${PROJECT}/${NAME}:${VERSION}
#推送云端
docker push  ${REGISTRY}/${PROJECT}/${NAME}:${VERSION}
#删除本地镜像
docker rmi  ${REGISTRY}/${PROJECT}/${NAME}:${VERSION}
