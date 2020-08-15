import { User } from '../services'
import { Radar } from '../misc/radar';

export default {
  id: {
    mysqlOptions: {
      filterable: true
    }
  },
  user: {
    __typename: User.__typename,
    mysqlOptions: {
      addable: true,
      joinInfo: {},
    },
    
  },
  location: {
    mysqlOptions: {
      addable: true,
    },
    resolver: async (context, req, currentObject, query, args, parent) => {
      console.log(currentObject)
      return Radar.getPlace(currentObject.location);
    }
  },
};