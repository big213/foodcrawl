import Service from '../service';

import generatePaginatorService from '../generator/paginator.service'

export class UserVisitedLocations extends Service {
  static __typename = 'userVisitedLocations';

  static paginator = generatePaginatorService(UserVisitedLocations);

  static presets = {
    default: {
      user: null,
      company: null,
      permissions: null
    }
  };

  static filterFieldsMap = {
    user: {
      field: "user",
    },
  };

  static isFilterRequired = true;

  static accessControl = {
    get: () => true,
    update: () => true,
    delete: () => true,
    getMultiple: () => true,
    add: () => true,
  }
};

