// 다운로드
docker pull openvidu/openvidu-dev:2.31.0
docker pull openvidu/openvidu-server-kms:2.23.0

// 컨테이너 제작(dev)
docker run --name openvidu ^
-p 4443:4443 --rm ^
-e OPENVIDU_SECRET=OnggiJonggi ^
-e DOMAIN_OR_PUBLIC_IP=192.168.150.27 ^
-e CERTIFICATE_TYPE=selfsigned ^
openvidu/openvidu-dev:2.31.0

docker run --name openvidu -p 4443:4443 --rm -e OPENVIDU_SECRET=OnggiJonggi -e DOMAIN_OR_PUBLIC_IP=192.168.150.27 -e CERTIFICATE_TYPE=selfsigned openvidu/openvidu-dev:2.31.0

docker run --name openvidu ^
  -d ^
  -p 4443:4443 ^
  -e OPENVIDU_SECRET=OnggiJonggi ^
  -e DOMAIN_OR_PUBLIC_IP=192.168.150.27 ^
  -e CERTIFICATE_TYPE=selfsigned ^
  openvidu/openvidu-dev:2.31.0


// 컨테이너 제작(kms), cmd
docker run -d --name openvidu ^
-p 4443:4443 ^
-e OPENVIDU_SECRET=OnggiJonggi ^
-e DOMAIN_OR_PUBLIC_IP=192.168.150.27 ^
-e CERTIFICATE_TYPE=owncert ^
openvidu/openvidu-server-kms:2.23.0

// 인증서 생성, cmd
"C:\Program Files\OpenSSL-Win64\bin\openssl.exe" req ^
  -x509 -newkey rsa:2048 ^
  -keyout certificate.key ^
  -out certificate.cert ^
  -days 365 -nodes ^
  -subj "/CN=192.168.150.27"
  
//인증서 복사
docker cp certificate.key openvidu:/opt/openvidu/owncert/certificate.key
docker cp certificate.cert openvidu:/opt/openvidu/owncert/certificate.cert



