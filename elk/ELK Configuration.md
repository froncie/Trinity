# `ELK System` with `MITRE ATT&CK`

### MITRE ATT&CK DOCUMENT

[how-to-use-mitre-attack.pdf](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/f9e21479-76a5-4036-a4ae-89a10976f871/how-to-use-mitre-attack.pdf)

#### Server INFO, Ubuntu 22.05, Ubuntu 용량 부족 문제

```bash
$ lvm
lvm> lvextend -l +100%FREE /dev/ubuntu-vg/ubuntu-lv
lvm> exit

$ resize2fs /dev/ubuntu-vg/ubuntu-lv
```

### Elastic Search - 8.2.2 version
  
  [[elastic-main]] 

``` bash 
 %% 1. Pull Elasticsearch Image%%
docker pull docker.elastic.co/elasticsearch/elasticsearch:8.2.2

 %% 2. Run the docker container %%
docker run -d --name elastic --net elastic -e "discovery.type=single-node" -e "ES_JAVA_OPTS=-Xmx1g -Xms1g" -p 9200:9200 -p 9300:9300 
docker.elastic.co/elasticsearch/elasticsearch:8.2.2

 %% 3. Configuration Password for elasticsearch %%
docker exec -it elastic "/bin/bash"
./bin/elasticsearch-setup-password interactive

%% 4. TLS Configuration %%
%% in elastic container %%
bin/elasticsearch-certutil ca

%% 4-1. Create CA twice for Elasticsearch and Kibana %%
bin/elasticsearch-certutil cert --ca elastic-stack-ca.p12

%% 4-2. HTTPS Configuration %%
bin/elasticsearch-certutil http
→ WE SHOULD SET DNS AND IP ADDRESSES!!

%% SET SECURE_PASSWORDs %%
bin/elasticsearch-keystore add xpack.security.http.ssl.keystore.secure_password

bin/elasticsearch-keystore add xpack.security.transport.ssl.keystore.secure_password

bin/elasticsearch-keystore add xpack.security.transport.ssl.truststore.secure_password

%% 4-3. MV AND UNZIP %%
mv {OUTPUT}.zip config/certs/
unzip {OUTPUT}.zip

%% 4-4. elasticsearch.yml %%
xpack.security.http.ssl:
  enabled: true
  keystore.path: certs/elasticsearch/http.p12
  
xpack.security.transport.ssl:
  enabled: true
  verification_mode: certificate
  keystore.path: certs/tls.p12
  truststore.path: certs/tls.p12

%% 4-5. elasticsearch.yml to Elasticsearch Container %%
docker cp elasticsearch.yml elastic:/usr/share/elasticsearch/config/

%% 5. kibana folder to Kibana Container %%
docker cp elastic:/usr/share/elasticsearch/config/certs/kibana ./

%% 5-1. MKDIR cert in Kibana Container %%
kibana@~: mkdir config/cert/

%% 5-2. kibana folder to Kibana Container %%
docker cp kibana kibana:/usr/share/kibana/config/cert

bin/kibana-keystore create

bin/kibana-keystore add server.ssl.keystore.password


%%6. TOKEN FOR Kibana %%

docker exec -it elastic "/bin/bash"
bin/elasticsearch-create-token --scope kibana
%% COPY the token %%
%% COPY the verification code IN kibana %%
bin/kibana-verification-code 
```

### LogStash - 8.2.2 version
``` Docker
docker pull docker.elastic.co/logstash/logstash:8.2.2 
```

```Docker
docker run (-d) --name logstash (--link elastic:elastic) -v {LOCAL CONFIG}:{CONTAINER  CONFIG} docker.elastic.co/logstash/logstash:8.2.2 -f {CONTAINER CONFIG}/OWN_PIPELINE_CONFIG   
```


### Kibana - 8.2.2 version

``` Docker
docker pull docker.elastic.co/kibana/kibana:8.2.2

docker run -d --name kibana --net elastic -p 5601:5601 docker.elastic.co/kibana/kibana:8.2.2
```

### Beats
```yaml
./filebeat -c snortbeat.yml
```
<br>
## [Elasticsearch, Kibana, Beats, Logstash, Configuration for maintaining SYSTEM secure using by SSL, TLS, HTTPS](https://www.elastic.co/kr/blog/configuring-ssl-tls-and-https-to-secure-elasticsearch-kibana-beats-and-logstash)

