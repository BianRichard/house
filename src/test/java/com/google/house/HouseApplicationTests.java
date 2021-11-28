package com.google.house;

import com.google.house.domain.*;
import com.google.house.form.HouseForm;
import com.google.house.form.PhotoForm;
import com.google.house.mapper.*;
import com.google.house.service.house.IHouseService;
import com.google.house.service.house.impl.IAddressServiceImpl;
import com.google.house.service.user.IUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class HouseApplicationTests {

    @Autowired
    private HouseDetailMapper houseDetailMapper;
    @Autowired
    private HouseMapper houseMapper;
    @Autowired
    private HousePictureMapper housePictureMapper;
    @Autowired
    private HouseTagMapper houseTagMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private SubwayMapper subwayMapper;
    @Autowired
    private SubwayStationMapper subwayStationMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SupportAddressMapper supportAddressMapper;
    @Autowired
    private IUserServiceImpl iUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IAddressServiceImpl addressService;
    @Autowired
    private HouseSubscribeMapper houseSubscribeMapper;
    @Autowired
    private IHouseService houseService;


    @Test
    void houseDetailMapperTest() {

//        System.out.println(houseDetailMapper.findByHouseId(33L));

//        List<Long> houseIds = new ArrayList<>();
//        houseIds.add(33L);
//        houseIds.add(34L);
//        houseDetailMapper.findAllByHouseIdIn(houseIds).forEach(System.out::println);

        houseDetailMapper.save(new HouseDetail(2L, "2", "2", "2", "2", 2, "2", 2L, 2L, "2", "2"));

    }

    @Test
    void houseMapperTest() {
//        houseMapper.updateCover(28L, "2");
//        houseMapper.updateStatus(28L, 2);
//        houseMapper.updateWatchTimes(28L);
//        houseMapper.save(new House("2", 2L, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, LocalDateTime.now(), LocalDateTime.now(), "2", "2", "2", "2", 2, "2", 2));

        System.out.println(houseMapper.findById(26L));

    }

    @Test
    void HousePictureMapperTest() {

        //housePictureMapper.findAllByHouseId(26L).forEach(System.out::println);

//        List<HousePicture> housePictures = new ArrayList<>();
//        housePictures.add(new HousePicture(1L,"1","1",1,1,"1"));
//        housePictures.add(new HousePicture(2L,"2","2",2,2,"2"));
//        housePictures.add(new HousePicture(3L,"3","3",3,3,"3"));
//
//        housePictureMapper.save(housePictures);

        System.out.println(housePictureMapper.findById(87L));

        housePictureMapper.deleteById(87L);

    }

    @Test
    void HouseTagMapperTest() {

//        System.out.println(houseTagMapper.findByNameAndHouseId("木棉6.0", 26L));
//
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//
//        houseTagMapper.findAllByHouseId(22L).forEach(System.out::println);
//
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//
//        List<Long> houseIds = new ArrayList<>();
//        houseIds.add(26L);
//        houseIds.add(27L);
//        houseTagMapper.findAllByHouseIdIn(houseIds).forEach(System.out::println);
//
//        List<HouseTag> houseTags = new ArrayList<>();
//        houseTags.add(new HouseTag(1L,"1"));
//        houseTags.add(new HouseTag(2L,"2"));
//        houseTagMapper.save(houseTags);

        houseTagMapper.deleteById(29L);

    }

    @Test
    void RoleMapperTest() {

        roleMapper.save(new Role(10L, "USER"));
        roleMapper.save(new Role(10L, "ADMIN"));

        roleMapper.findRolesByUserId(10L).forEach(System.out::println);

    }

    @Test
    void SubwayMapperTest() {
        subwayMapper.findAllByCityEnName("bj").forEach(System.out::println);
    }

    @Test
    void SubwayStationMapperTest() {
        subwayStationMapper.findAllBySubwayId(9L).forEach(System.out::println);
    }

    @Test
    void UserMapperTest() {

        //userMapper.save(new User("Bill","bill123","bill@qq.com","16978541232",0, LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now(),null));

        System.out.println(userMapper.findOne(5L) + "\n!!!!!!!!!!!!!!!!!!!");

        System.out.println(userMapper.findByName("Bill") + "\n!!!!!!!!!!!!!!!!!!!");

        System.out.println(userMapper.findUserByPhoneNumber("16978541232") + "\n!!!!!!!!!!!!!!!!!!!");

        userMapper.updateUsername(5L, "bill");

        userMapper.updateEmail(5L, "Bill@qq.com");

        userMapper.updatePassword(5L, "bill1234");

    }


    @Test
    void supportAddressMapper() {

//        supportAddressMapper.findAllByLevel("city").forEach(System.out::println);
//        System.out.println(supportAddressMapper.findByEnNameAndLevel("szs", "city"));
//        System.out.println(supportAddressMapper.findByEnNameAndBelongTo("szs", "广东省"));
//        supportAddressMapper.findAllByLevelAndBelongTo("region","深圳市").forEach(System.out::println);
//        System.out.println(supportAddressMapper.findByEnName("szs"));
//        supportAddressMapper.findAllByEnNameAndBelongTo("szs", "深圳市").forEach(System.out::println);
    }

    @Test
    void houseSubscribeMapperTest() {

//        System.out.println(houseSubscribeMapper.findByHouseIdAndUserId(26L, 2L));

//        houseSubscribeMapper.findAllByUserIdAndStatus(3L,2).forEach(System.out::println);

//        houseSubscribeMapper.findAllByAdminIdAndStatus(1L,2).forEach(System.out::println);

//        System.out.println(houseSubscribeMapper.findByHouseIdAndAdminId(26L, 1L));

        houseSubscribeMapper.updateStatus(16L, 3);

    }

    @Test
    void IUserServiceImplTest() {
        System.out.println(iUserService.findUserByName("132****1231"));
        System.out.println(iUserService.findUserByTelephone("13269541231"));
        System.out.println(iUserService.findById(7L));
        //iUserService.adduserByPhone("13269541231");
    }

    @Test
    void passwordEncoderTest() {
        System.out.println(passwordEncoder.encode("12345"));
        System.out.println(passwordEncoder.encode("jerry123"));
    }

    @Test
    void addressServiceTest() {
//        System.out.println(addressService.findAllCities());
//        System.out.println(supportAddressMapper.findByEnNameAndLevel("szs", "city"));
//        System.out.println(addressService.findAllRegionsByCityName("szs"));
//        System.out.println(addressService.findCity("szs"));
//        addressService.findAllSubwayByCity("bj").forEach(System.out::println);
//        addressService.findAllStationBySubway(9L).forEach(System.out::println);
//        System.out.println(addressService.findSubway(9L));
        System.out.println(addressService.findSubwayStation(66L));
    }

    @Test
    void saveHouse() {
        HouseForm houseForm = new HouseForm();
        houseForm.setTitle("title");
        houseForm.setCityEnName("bj");
        houseForm.setRegionEnName("dcq");
        houseForm.setStreet("这是一个街道");
        houseForm.setDistrict("小区");
        houseForm.setDetailAddress("这是一个地址 chaoyangqu ");
        houseForm.setRoom(2);
        houseForm.setParlour(2);
        houseForm.setFloor(10);
        houseForm.setTotalFloor(30);
        houseForm.setDirection(1);
        houseForm.setBuildYear(2020);
        houseForm.setArea(100);
        houseForm.setPrice(1000000);
        houseForm.setRentWay(0);
        houseForm.setSubwayLineId(1L);
        houseForm.setSubwayStationId(5L);
        houseForm.setDistanceToSubway(1000);
        houseForm.setBathroom(1);
        houseForm.setLayoutDesc("fdsfads");
        houseForm.setRoundService("houseForm");
        houseForm.setTraffic("fdsfdsa");
        houseForm.setDescription("fdsfdsadsa");
        houseForm.setCover("fdsa");

//        List<String> houseTags = new ArrayList<>();
//        houseTags.add("123");
//        houseTags.add("123");
//        houseForm.setTags(houseTags);

        List<PhotoForm> tags = new ArrayList<>();
        PhotoForm photoForm = new PhotoForm();
        photoForm.setPath("1");
        photoForm.setWidth(1);
        photoForm.setHeight(1);
        tags.add(photoForm);
        houseForm.setPhotos(tags);

        ArrayList<String> objects = new ArrayList<>();
        objects.add("1");
        houseForm.setTags(objects);

        houseService.save(houseForm);
//        System.out.println(houseService.save(houseForm).getResult());
    }

    @Test
    void contextLoads() {

    }

}
