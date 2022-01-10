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
