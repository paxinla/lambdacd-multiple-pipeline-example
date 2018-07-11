# An example of using multiple pipeline in one LambdaCD instance.

An example based by [the demo code written by flosell](https://github.com/flosell/lambdacd-demo-pipeline) , I use this skeleton in my work.

## Usage

- Run `lein run` in this direcotory, an http server will start listening on port 9081 on loalhost. You can change this in `core.clj` .
- Run `lein uberjar` in this direcotory, a fat jar will be generated under target directory. Then you can execute `java -jar multiple_pipeline_example-0.1.0-SNAPSHOT-standalone.jar` to start the http server.


I use the second way to deploy the LambdaCD in a server, and use nginx as the proxy. The following options needed in nginx for LambdaCD:
```
server {
    ... ...

    # tell http-kit to keep the connection
    proxy_http_version 1.1;
    proxy_set_header Connection "";
    proxy_set_header Upgrade $http_upgrade;
    
    # Web socket
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header Host $http_host;
    proxy_set_header X-Real-IP $remote_addr;
}
```

## Add new pipeline

To add an new pipeline, just write it in a new .clj file under the `pipelines` directory, and add its config in `pipeline-configs` in the `index.clj` .


## Git support

I use git with HTTPS in the example_two, it use OS environment variable `LAMBDACD_GITHUB_USERNAME` and `LAMBDACD_GITHUB_PASSWORD` for the authentication for HTTPS .
