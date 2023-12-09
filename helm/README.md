# Deploy to Kubernetes ([Docker Desktop for Windows](https://hub.docker.com/editions/community/docker-ce-desktop-windows))

Docker version used:
```
C:\>docker version
Client:
 Cloud integration: v1.0.35+desktop.5
 Version:           24.0.6
 API version:       1.43
 Go version:        go1.20.7
 Git commit:        ed223bc
 Built:             Mon Sep  4 12:32:48 2023
 OS/Arch:           windows/amd64
 Context:           default

Server: Docker Desktop 4.25.0 (126437)
 Engine:
  Version:          24.0.6
  API version:      1.43 (minimum version 1.12)
  Go version:       go1.20.7
  Git commit:       1a79695
  Built:            Mon Sep  4 12:32:16 2023
  OS/Arch:          linux/amd64
  Experimental:     false
 containerd:
  Version:          1.6.22
  GitCommit:        8165feabfdfe38c65b599c4993d227328c231fca
 runc:
  Version:          1.1.8
  GitCommit:        v1.1.8-0-g82f18fe
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
docker tag hello-okta-api localhost:5000/hellooktaapi
docker tag hello-okta-bff localhost:5000/hellooktabff
docker tag hello-okta-spa-web localhost:5000/hellooktaspa
```
`hello-okta-spa-web` Docker image can be build using instructions from [here](https://github.com/gennadyyonov/hello-okta-spa).

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
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.7.0/aio/deploy/recommended.yaml
```

[Create Sample User](https://github.com/kubernetes/dashboard/blob/master/docs/user/access-control/creating-sample-user.md)

Run the following scripts (copied from link above) from [sample-user](sample-user) folder:
```
kubectl apply -f dashboard-adminuser-service-account.yaml
kubectl apply -f dashboard-adminuser-cluster-role-binding.yaml
kubectl apply -f dashboard-secret.yaml
```

After Secret is created, we can execute the following command (in bash) to get the token which saved in the Secret:
```
kubectl get secret admin-user -n kubernetes-dashboard -o jsonpath={".data.token"} | base64 -d
```

```
kubectl proxy
```
Dashboard will be available at [URL](http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/)

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
```
```
helm install nginx-ingress ingress-nginx/ingress-nginx
```
or
```
helm upgrade nginx-ingress ingress-nginx/ingress-nginx
```

### Cert Manager

- [Installing with Helm](https://cert-manager.io/docs/installation/kubernetes/#installing-with-helm)
```
helm repo add jetstack https://charts.jetstack.io
helm repo update
kubectl apply -f https://github.com/jetstack/cert-manager/releases/download/v1.13.2/cert-manager.crds.yaml
```

- Create namespace for `cert-manager`
```
kubectl create namespace cert-manager
```

- Install `cert-manager` Helm chart
```
helm install cert-manager jetstack/cert-manager --namespace cert-manager --version v1.13.2
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
Application can be accessed via URL: https://kubernetes.docker.internal/. 
It may take some time to have it up and running and accessible (check logs).

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