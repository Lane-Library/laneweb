[![Build Status](https://drone.med.stanford.edu/api/badges/irt-lane/laneweb/status.svg)](https://drone.med.stanford.edu/xuwang/docker-nodeapp)

# Laneweb WAR in Docker

## Build Laneweb image

### Clone the account repo and setup credentials

```
$ mkdir -p $HOME/projects/lane
$ cd $HOME/projects/lane
$ git clone git@gitlab.med.stanford.edu:irt-lane/accounts.git
$ cd accounts
$ git-crypt unlock
```
### Clone the laneweb repo and build war
    
```
$ cd $HOME/projects/lane
$ git clone git@gitlab.med.stanford.edu:irt-lane/laneweb.git
$ cd laneweb

$ make build
```

### Build docker image
    
```
$ make build_docker
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

This repo support [DroneCI](https://drone.med.stanford.edu/irt-lane/laneweb).

To setup the deployment key before git commit and push for ci:

```
$ make sec
```
