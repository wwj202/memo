# java application
#构建参数
NAME=appName
PROJECT=prjName
VERSION=0.1
REGISTRY=registry.xxx.com
USER=user1
PWD=password1

#生产start.sh文件
cat > start.sh << EOF
#!/bin/sh
/usr/local/tomcat/bin/startup.sh
tail -f /usr/local/tomcat/logs/catalina.out
EOF

#构建镜像
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
#登录仓库
docker login   -u ${USER} -p ${PWD}  ${REGISTRY}
#打TAG
docker tag ${NAME}:${VERSION}   ${REGISTRY}/${PROJECT}/${NAME}:${VERSION}
#推送云端
docker push  ${REGISTRY}/${PROJECT}/${NAME}:${VERSION}
#删除本地镜像
docker rmi  ${REGISTRY}/${PROJECT}/${NAME}:${VERSION}


# web application
## STEP 1
npm config set @jd:registry http://registry.xxx.com
npm install
npm run build

## STEP 2
#!/bin/bash
#rancher env SPRING_PROFILES_ACTIVE = dev
#工程名称
NAME=appName
#仓库项目名称
PROJECT=projectName

#版本号
VERSION=1.0.2
TIME=$(date "+%Y%m%d%H%M%S")
REGISTRY=registry.xxx.com
#用户名密码
USER=user1
PWD=password1

cat > index.html << EOF
<!DOCTYPE html></html><head><meta http-equiv="refresh" content="0;url=/ui"></head><body>source2.0</body></html>
EOF

cat > nginx.conf << EOF
user  root;
worker_processes  1;

error_log  /var/log/nginx/error.log warn;
pid        /var/run/nginx.pid;

events {	
    worker_connections  1024;
}

http {
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;

    log_format access_json '{"http_cookie":"\$http_cookie","document_root":"\$document_root","uri":"\$uri","args":"\$args","host":"\$host","server_addr":"\$server_addr"'
    ',"remote_addr":"\$remote_addr","remote_port":"\$remote_port","remote_user":"\$remote_user","time_local":"\$time_local","request":"\$request"'
    ',"status":"\$status","body_bytes_sent":"\$body_bytes_sent","http_referer":"\$http_referer","request_method":"\$request_method","status":"\$status"'
    ',"user_agent":"\$http_user_agent","up_addr":"\$upstream_addr","up_host":"\$upstream_http_host","scheme":"\$scheme","server_port":"\$server_port"'
    ',"upstream_time":"\$upstream_response_time","request_time":"\$request_time","request_uri":"\$request_uri","server_protocol":"\$server_protocol"'
    ',"http_x_forwarded_for":"\$http_x_forwarded_for","hostname":"\$hostname","server_name":"\$server_name"}';
        
    access_log  /var/log/nginx/access.log  access_json;

    sendfile        on;

    keepalive_timeout  65;
    
    include /etc/nginx/conf.d/*.conf;
}
EOF


cat > default.conf << EOF
server {
	listen 80;
	server_name xxx.xxx.com;

	#charset koi8-r;
	#access_log /var/log/nginx/smartidc.access.log;
	error_log /var/log/nginx/error.log error;

	location / {
		root /usr/share/nginx/html;
		index index.html index.htm;
	}

	#error_page 404 /404.html;
	error_page   404 =200 /page404.html;

    location /page404.html {
        set \$ret_body '{"code": "200","msg": "set access log"}';
        if ( \$request_uri ~* "/setAccessLog" ){
            return 200 \$ret_body;
        }
    }
	# redirect server error pages to the static page /50x.html
	#
	error_page 500 502 503 504 /50x.html;
	location = /50x.html {
		root /usr/share/nginx/html;
	}

	location /v1.0 {
		proxy_pass http://xxx.yyy.com/v1.0;
		client_max_body_size 1024m;
		proxy_connect_timeout 300s;
		proxy_send_timeout 300s;
		proxy_read_timeout 300s;
	}
}
EOF

#构建镜像
cat > Dockerfile << EOF
FROM nginx:1.16.1
ENV TZ Asia/Shanghai
COPY dist/ /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
COPY default.conf /etc/nginx/conf.d/default.conf
EOF

#本地build镜像
docker build -t ${NAME}:${VERSION} .
#登陆docker
docker login   -u ${USER} -p ${PWD}  ${REGISTRY}
#打TAG
docker tag ${NAME}:${VERSION}   ${REGISTRY}/${PROJECT}/${NAME}:${VERSION}
#推送云端
docker push  ${REGISTRY}/${PROJECT}/${NAME}:${VERSION}
#删除本地镜像
docker rmi -f ${REGISTRY}/${PROJECT}/${NAME}:${VERSION}

#rancher kubectl set image deployment/test-node-js nodejs-02=registry.xxx.com/dcm_dev/nodejs-02:latest -n harbor
#kubectl set image deployment/appname-prod source2ui-prod=${REGISTRY}/${PROJECT}/${NAME}:${VERSION} -n ui-prod
