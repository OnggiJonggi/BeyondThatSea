// 포트 4443, 3478, 40000-42000 TCP,UDP 포트 열어두기

// 다운로드
docker pull openvidu/openvidu-server-kms:2.23.0

// .env 파일 작성
DOMAIN_OR_PUBLIC_IP=localhost
OPENVIDU_SECRET=OnggiJonggi
CERTIFICATE_TYPE=selfsigned

// docker-compose.yml 파일 작성
services:
  openvidu-server:
    image: openvidu/openvidu-server-kms:2.23.0
    container_name: openvidu-server
    restart: unless-stopped
    ports:
      - "4443:4443"
      - "3478:3478"
      - "40000-42000:40000-42000"
    environment:
      - DOMAIN_OR_PUBLIC_IP=${DOMAIN_OR_PUBLIC_IP}
      - OPENVIDU_SECRET=${OPENVIDU_SECRET}
      - CERTIFICATE_TYPE=${CERTIFICATE_TYPE}

//docker-compose 실행
cd D:\Dev\Docker\openvidu
docker compose up -d


// 건방지게 docker가 유령 컨테이너를 깨작거린다면(관리자 권한)
net stop com.docker.service
net start com.docker.service

// 더 건방지게 docker가 무한 로딩을 깨작거린다면
wsl --unregister docker-desktop
wsl --unregister docker-desktop-data
// 컴퓨터 재시작
