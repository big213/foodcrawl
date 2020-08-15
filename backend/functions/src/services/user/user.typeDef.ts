import { UserVisitedLocations } from '../services'
import { Radar } from '../misc/radar';

export default {
  id: {
    mysqlOptions: {
      filterable: true
    }
  },
  radar_id: {
    resolver: async (context, req, currentObject, query, args, parent) => {
      return UserVisitedLocations.paginator.getRecord(req, {
        ...query?.__args,
        user: currentObject.id
      }, query);
    }
  },
  name: {
    mysqlOptions: {
      addable: true,
      updateable: true
    }
  },
  visitedLocations: {
    __typename: UserVisitedLocations.__typename,
    resolver: async (context, req, currentObject, query, args, parent) => {
      return UserVisitedLocations.paginator.getRecord(req, {
        ...query?.__args,
        user: currentObject.id
      }, query);
    }
  },
  randomNearbyNewPlace: {
    resolver: async (context, req, currentObject, query, args, parent) => {
      return Radar.getRandomNearbyNewPlace(currentObject.id, ["food-beverage"]);
    }
  }
}