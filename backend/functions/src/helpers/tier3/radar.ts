import mysqlHelper from '../tier1/mysql';
import errorHelper from '../tier0/error';

export async function handleRadarWebhook(req, res) {
  if(req.body.type === "user.entered_place") {
    //get the user_id from the radar_id
    const userResults = await mysqlHelper.executeDBQuery("SELECT id FROM user WHERE radar_id = :radar_id", {
      radar_id: req.body.user._id
    });

    if(userResults.length < 1) {
      throw errorHelper.generateError("User not found");
    }

    //add the location id to the list of places the user has been
    await mysqlHelper.executeDBQuery("INSERT IGNORE INTO userVisitedLocations SET user = :user, location = :location", {
      user: userResults[0].id,
      location: req.body.place._id
    });
  }
  res.send({});
}