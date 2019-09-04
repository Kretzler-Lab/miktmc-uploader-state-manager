# stateManagerService

The state manager service will do the work to keep track of the state of packages as they travel from the data lake to the knowledge environment.

State manager will run in its own docker-compose script (found in the heavens-docker/stateManager repo on gitHub) and will run on the same network as orion.

To run on your local machine:
1) Navigate to your heavens-docker/orion
2) docker-compose -f docker-compose.dev.yml up -d
3) Navigate to heavens-docker/stateManager
4) docker-compose -f docker-compose.dev.yml up -d
