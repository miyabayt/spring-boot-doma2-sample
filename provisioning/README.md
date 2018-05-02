# Sample provisioning

Teraformを用いる

## access_key / secret_key

terraform.tfvarsに記載

```
$ cat terraform.tfvars
access_key="[アクセスキー記載]"
secret_key="[シークレットキーを記載]"
aws_id="[AWS IDを記載]"
```

## plan

```
terraform plan
```

## apply

```
terraform apply
```

## show

```
terraform show
```

## destory :boom:

```
terraform destroy
```
