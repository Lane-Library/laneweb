# Laneweb WAR in Docker

## Prerequisites

1. **[Install Docker](https://www.docker.com/products/docker)**

1. **[Install Drone command line tools](http://readme.drone.io/devs/cli/)**

## Build Laneweb image

### Clone the laneweb repo and build app war
    
```
$ cd $HOME/projects/lane
$ git clone git@gitlab.med.stanford.edu:lane/laneweb.git
$ cd laneweb

$ make docker
```

## Push to repo

```
$ make push
```

## Pull the latest image from repo

```
$ make pull
```
## CI/CD Support

#### Get and setup personal gitlab access token
Gitlab API requires access token to talk to gitlab server.

Please get your personal token from https://gitlab.med.stanford.edu/-/profile/personal_access_tokens
and save the the token to ${HOME}/.gitlab-token file.

_NOTE_: Do not add newline at the end of the token.

#### Setup gitlab pipeline and slack notification

```
$ make gl-setup
```

