import axios from 'axios'
import * as functions from 'firebase-functions';
import mysqlHelper from '../../helpers/tier1/mysql';

const env = process.env.DEV ? require('../../../../env.json') : functions.config();

import errorHelper from '../../helpers/tier0/error';

const radarApi = axios.create({
  baseURL: "https://api.radar.io/v1",
  headers: {
    Authorization: env.radar.test_secret
  }
});

export class Radar {
  static doAction() {
    return radarApi.get("/search/places?categories=food-beverage&near=40.78382,-73.97779");
  }

  static async getNearbyPlaces(categories: Array<string>, latitude: Number, longitude: Number) {
    if(!categories.length) {
      throw errorHelper.missingParamsError();
    }

    const { data } = await radarApi.get("/search/places?categories=" + categories.join(",") + "&near=" + latitude + "," + longitude);

    return data.places;
  }

  static async getRandomNearbyNewPlace(userId: Number, categories: Array<string>) {
    if(!categories.length || !userId) {
      throw errorHelper.missingParamsError();
    }

    //get the radar_id
    const userResult = await mysqlHelper.executeDBQuery("SELECT radar_id FROM user WHERE id = :id", {
      id: userId
    });

    //get the user data from radar
    const radarResult = await radarApi.get("/users/" + userResult[0].radar_id);

    const { data: { places: nearbyPlaces } } = await radarApi.get("/search/places?categories=" + categories.join(",") + "&near=" + radarResult.data.user.location.coordinates[1] + "," + radarResult.data.user.location.coordinates[0]);

    if(!nearbyPlaces[0]) {
      throw errorHelper.generateError("No places nearby");
    }

    //get ids of places the user has visited
    const placeVisitedResult = await mysqlHelper.executeDBQuery("SELECT location FROM userVisitedLocations WHERE user = :user", {
      user: userId
    });

    const placeVisitedIds = placeVisitedResult.map(item => item.location);

    //only add to this array if not visited before
    const newNearbyPlaces = nearbyPlaces.reduce((total, item) => {
      if(!placeVisitedIds.includes(item._id)) {
        total.push(item)
      }
      return total;
    }, []);

    return newNearbyPlaces[Math.floor(Math.random()*newNearbyPlaces.length)];
  }

  static async getPlace(placeId) {
    if(!placeId) {
      throw errorHelper.missingParamsError();
    }

    const { data } = await radarApi.get("/places/" + placeId);

    return data.place;
  }


  static async getGeofence(geofenceId) {
    if(!geofenceId) {
      throw errorHelper.missingParamsError();
    }

    const { data } = await radarApi.get("/geofences/" + geofenceId);

    return data.place;
  }

  static async createGeofence(description: string, latitude: Number, longitude: Number) {
    const { data } = await radarApi.post("/geofences", {
      description: description,
      coordinates: [latitude, longitude],
      radius: 50,
      type: "circle"
    });

    return data.geofence;
  }

  //static async createGeoFence() {
  //
  //}
};