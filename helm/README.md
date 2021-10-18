# Deploy to Kubernetes ([Docker Desktop for Windows](https://hub.docker.com/editions/community/docker-ce-desktop-windows))

Docker version used:
```
C:\>docker version
Client:
 Cloud integration: 1.0.14
 Version:           20.10.6
 API version:       1.41
 Go version:        go1.16.3
 Git commit:        370c289
 Built:             Fri Apr  9 22:49:36 2021
 OS/Arch:           windows/amd64
 Context:           default
 Experimental:      true

Server: Docker Engine - Community
 Engine:
  Version:          20.10.6
  API version:      1.41 (minimum version 1.12)
  Go version:       go1.13.15
  Git commit:       8728dd2
  Built:            Fri Apr  9 22:44:56 2021
  OS/Arch:          linux/amd64
  Experimental:     false
 containerd:
  Version:          1.4.4
  GitCommit:        05f951a3781f4f2c1911b05e61c160e9c30eaa8e
 runc:
  Version:          1.0.0-rc93
  GitCommit:        12644e614e25b05da6fd08a38ffa0cfe1903fdec
 docker-init:
  Version:          0.19.0
  GitCommit:        de40ad0
```

## Deploy Local Registry Server

- [Run a local registry](https://docs.docker.com/registry/)
```
docker run -d -p 5000:5000 --name registry registry:2
```
or just start if already exists:
```
docker start registry
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
[Kubernetes Dashboard](https://github.com/kubernetes/dashboard)

```
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.2.0/aio/deploy/recommended.yaml
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

### Cert Manager

- [Installing with Helm](https://cert-manager.io/docs/installation/kubernetes/#installing-with-helm)
```
helm repo add jetstack https://charts.jetstack.io
helm repo update
kubectl apply -f https://github.com/jetstack/cert-manager/releases/download/v1.3.1/cert-manager.crds.yaml
```

- Create namespace for `cert-manager`
```
kubectl create namespace cert-manager
```

- Install `cert-manager` Helm chart
```
helm install cert-manager jetstack/cert-manager --namespace cert-manager --version v1.3.1
```

### Install Application

- **Application Properties**

- Copy [`secret-values.yaml.sample`](hello-okta/secret-values.yaml.sample) to `secret-values.yaml` under `hello-okta`
- Fill in your configuration properties instead of `???`
- Either install release:
    ```
    helm install hello-okta-release ./helm/hello-okta --values ./helm/hello-okta/values.yaml --values ./helm/hello-okta/secret-values.yaml --create-namespace --namespace hello-okta
    ```
    or upgrade release if already installed:
    ```
    helm upgrade hello-okta-release ./helm/hello-okta --values ./helm/hello-okta/values.yaml --values ./helm/hello-okta/secret-values.yaml --create-namespace --namespace hello-okta
    ```

Workloads can be accessed in [Kubernetes Dashboard](http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/#/overview?namespace=hello-okta)

### Uninstall Application
```
helm uninstall hello-okta-release --namespace hello-okta
```

### Debug Helm Template

```
helm install --dry-run --debug hello-okta-release ./helm/hello-okta --values ./helm/hello-okta/values.yaml --values ./helm/hello-okta/secret-values.yaml --create-namespace --namespace hello-okta
```
or
```
helm template --debug hello-okta-release ./helm/hello-okta --values ./helm/hello-okta/values.yaml --values ./helm/hello-okta/secret-values.yaml --create-namespace --namespace hello-okta
```

### Restart Pods
```
kubectl rollout restart deployment/hello-okta-api -n hello-okta
kubectl rollout restart deployment/hello-okta-bff -n hello-okta
kubectl rollout restart deployment/hello-okta -n hello-okta
```

### Useful Resources

* [kubectl Cheat Sheet](https://kubernetes.io/docs/reference/kubectl/cheatsheet/)