[![Build Status](https://ci.med.stanford.edu/api/badges/lane/laneweb/status.svg)](https://ci.med.stanford.edu/lane/laneweb)

# Laneweb WAR in Docker

## Prerequisites

1. **[Install Docker](https://www.docker.com/products/docker)**

1. **[Install Drone command line tools](http://readme.drone.io/devs/cli/)**

## Build Laneweb image

### Clone the account repo and setup credentials

```
$ mkdir -p $HOME/projects/lane
$ cd $HOME/projects/lane
$ git clone git@gitlab.med.stanford.edu:irt-lane/accounts.git
$ cd accounts
$ git-crypt unlock
```
### Clone the laneweb repo and build app war
    
```
$ cd $HOME/projects/lane
$ git clone git@gitlab.med.stanford.edu:lane/laneweb.git
$ cd laneweb

$ make app
```

### Build docker image
    
```
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

This repo supports [DroneCI](https://ci.med.stanford.edu/lane/laneweb).

#### Get and setup personal drone token
Drone CLI requires access token to talk to drone server.

Please get your personal token from https://ci.med.stanford.edu/account/token, 
and save the the token to ${HOME}/.drone-token file. 

_NOTE_: Do not add newline at the end of the token.

#### To turn on the ci job defined in .drone.yml

```
$ make drone-setup
```

