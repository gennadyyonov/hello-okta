# Deploy to Kubernetes (Docker Desktop)

## Deploy Local Registry Server

- [Run a local registry](https://docs.docker.com/registry/)
```
docker run -d -p 5000:5000 --name registry registry:2
```

## Copy Images to Local Registry

- Tag images 
```
docker tag hello-okta_api localhost:5000/hellooktaapi
docker tag hello-okta_bff localhost:5000/hellooktabff
docker tag hello-okta-spa_web localhost:5000/hellooktaspa
```
`hello-okta-spa_web` Docker image can be build using instructions from [here](https://github.com/gennadyyonov/hello-okta-spa).

- Push images
```
docker push localhost:5000/hellooktaapi
docker push localhost:5000/hellooktabff
docker push localhost:5000/hellooktaspa
```

## Access Applications in a Cluster

[Kubernetes Web UI (Dashboard)](https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard/)

```
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0/aio/deploy/recommended.yaml
kubectl proxy
```
Dashboard will be available at [URL](http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/)

If `Not enough data to create auth info structure` is displayed trying to log-in using Kubeconfig (`C:\Users\<USER>\.kube\config`)
execute the following script in bash:
```
#!/bin/bash
TOKEN=$(kubectl -n kube-system describe secret default| awk '$1=="token:"{print $2}')
kubectl config set-credentials docker-desktop --token="${TOKEN}"
```

### Helm

Helm is the package manager for Kubernetes, see [Quickstart Guide](https://helm.sh/docs/intro/quickstart/).
```
helm repo add stable https://charts.helm.sh/stable
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
```

### [Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress/)

Ingress exposes HTTP and HTTPS routes from outside the cluster to services within the cluster. Traffic routing is controlled by rules defined on the Ingress resource.

```
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
helm install nginx-ingress ingress-nginx/ingress-nginx
```

### TLS/SSL

- [Deploy cert-manager CRD](https://cert-manager.io/docs/installation/kubernetes/)
```
helm repo add jetstack https://charts.jetstack.io
helm repo update
kubectl apply -f https://github.com/jetstack/cert-manager/releases/download/v1.1.0/cert-manager.crds.yaml
```

- Create namespace for cert-manager
```
kubectl create namespace `cert-manager`
```

- Install `cert-manager` Helm chart
```
helm install cert-manager jetstack/cert-manager --namespace cert-manager --version v1.0.1
```

### Install Application
```
helm install hello-okta-release ./helm/hello-okta --values ./helm/hello-okta/values.yaml
```

### Uninstall Application
```
helm uninstall hello-okta-release
```