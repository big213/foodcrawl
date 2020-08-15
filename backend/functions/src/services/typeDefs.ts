import generatePaginatorTypeDef from './generator/paginator.typeDef'

import { User, UserVisitedLocations } from './services'

import user from './user/user.typeDef'

import userVisitedLocations from './link/userVisitedLocations.typeDef'

export const typeDefs = {
  user,
  userPaginator: generatePaginatorTypeDef(User),
  userVisitedLocations,
  userVisitedLocationsPaginator: generatePaginatorTypeDef(UserVisitedLocations),
}