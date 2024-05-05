package bus.maapstrichtfinal.Map.routing;

//import com.graphhopper.GHRequest;
//import com.graphhopper.GHResponse;
//import com.graphhopper.GraphHopper;
//import com.graphhopper.ResponsePath;
//import com.graphhopper.config.CHProfile;
//import com.graphhopper.config.Profile;
//import com.graphhopper.util.Instruction;
//import com.graphhopper.util.InstructionList;
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class RoutingService {
//
//    private static RoutingService instance;
//    private final GraphHopper graphHopper;
//
//    public static RoutingService getInstance() {
//
//        if (instance == null) {
//            instance = new RoutingService();
//        }
//        return instance;
//    }
//
//    private RoutingService() {
//        graphHopper = createGraphHopperInstance("java-map/data/limburg-latest.osm");
//    }
//
//    private GraphHopper createGraphHopperInstance(String ghLocalisation) {
//
//        GraphHopper graphHopper = new GraphHopper();
//        graphHopper.setOSMFile(ghLocalisation);
//
//        // specify where to store graphhopper files
//        graphHopper.setGraphHopperLocation("target/routing-graph-cache");
//
//        // see docs/core/profiles.md to learn more about profiles
//        graphHopper.setProfiles(new Profile("car").setVehicle("car").setWeighting("fastest").setTurnCosts(false));
//
//        // this enables speed mode for the profile we called car
//        graphHopper.getCHPreparationHandler().setCHProfiles(new CHProfile("car"));
//
//        // now this can take minutes if it imports or a few seconds for loading of course this is dependent on the area you import
//        graphHopper.importOrLoad();
//        return graphHopper;
//    }
//
//    public List<RoutingData> routing(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
//
//        GHRequest request = new GHRequest(startLatitude, startLongitude, endLatitude, endLongitude).
//                //we have to specify which profile we are using even when there is only one
//                setProfile("car");
//
//        GHResponse response = graphHopper.route(request);
//
//        // handle errors
//        if (response.hasErrors()) {
//            throw new RuntimeException(response.getErrors().toString());
//        }
//
//        // use the best path, see the GHResponse class for more possibilities.
//        ResponsePath path = response.getBest();
//
//
//        InstructionList il = path.getInstructions();
//
//        // iterate over all turn instructions
//        List<RoutingData> list = new ArrayList<>();
//        for (Instruction instruction : il) {
//            list.add(new RoutingData(instruction.getPoints()));
//        }
//        return list;
//    }
//}
