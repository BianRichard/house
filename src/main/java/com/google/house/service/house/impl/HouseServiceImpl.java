package com.google.house.service.house.impl;

import com.google.house.base.ServiceMultiResult;
import com.google.house.base.ServiceResult;
import com.google.house.domain.*;
import com.google.house.dto.HouseDTO;
import com.google.house.dto.HouseDetailDTO;
import com.google.house.dto.HousePictureDTO;
import com.google.house.form.DatatableSearch;
import com.google.house.form.HouseForm;
import com.google.house.form.PhotoForm;
import com.google.house.form.RentSearch;
import com.google.house.mapper.*;
import com.google.house.base.HouseStatus;
import com.google.house.service.house.IHouseService;
import com.google.house.service.house.IQiNiuService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HouseServiceImpl implements IHouseService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private HouseDetailMapper houseDetailMapper;

    @Autowired
    private HousePictureMapper housePictureMapper;

    @Autowired
    private HouseTagMapper houseTagMapper;

    @Autowired
    private SubwayMapper subwayMapper;

    @Autowired
    private SubwayStationMapper subwayStationMapper;

    @Autowired
    private IQiNiuService qiNiuService;

    @Value("${qiniu.cdn.prefix}")
    private String cdnPrefix;

    /**
     * 添加一个房源
     * @param houseForm
     * @return
     */
    @Override
    @Transactional
    public ServiceResult<HouseDTO> save(HouseForm houseForm) {

        //完成houseForm中的属性添加到house_detail
        HouseDetail detail = new HouseDetail();
        ServiceResult<HouseDTO> subwayValidationResult = wrapperDetailInfo(detail, houseForm);
        //wrapperDetailInfo()完成赋值返回null
        //wrapperDetailInfo()没有完成赋值,直接返回ServiceResult.of(false, "Not valid subway line!");
        if (subwayValidationResult != null) {
            return subwayValidationResult;
        }
        //运行到此处表示house_detail大部分内容完成赋值




        //完成添加到house
        House house = new House();
        modelMapper.map(houseForm, house);
        //继续完成house的添加,这些信息需要手动设置,无法从网页获取
        LocalDateTime now = LocalDateTime.now();
        house.setCreateTime(now);
        house.setLastUpdateTime(now);
//      house.setAdminId(LoginUserUtil.getLoginUserId());
        house.setAdminId(1L);
        houseMapper.save(house);




        //当house完成添加完成
        //需要给house_detail的house_id赋值,即house_detail中对应哪个house
        //完成了添加到house_detail
        detail.setHouseId(house.getId());
        houseDetailMapper.save(detail);



        //generatePictures:HouseForm中上传到网页的图片到HousePicture实体的赋值
        //house_picture完成批量添加
        List<HousePicture> pictures = generatePictures(houseForm, house.getId());
        housePictureMapper.save(pictures); //注意mysql batch




        //这一个和下面的都是将所有已经添加的信息再添加到HouseDTO
        //将house中的属性添加到houseDTO
        HouseDTO houseDTO = modelMapper.map(house, HouseDTO.class);
        //将house中的属性添加到HouseDetailDTO
        HouseDetailDTO houseDetailDTO = modelMapper.map(detail, HouseDetailDTO.class);
        //将HouseDetailDTO设置到houseDTO中
        houseDTO.setHouseDetail(houseDetailDTO);





        //将pictures中的属性添加到HousePictureDTO
        //再将HousePictureDTO的属性添加到houseDTO
        List<HousePictureDTO> pictureDTOS = new ArrayList<>();
        pictures.forEach(housePicture -> pictureDTOS.add(modelMapper.map(housePicture, HousePictureDTO.class)));
        houseDTO.setPictures(pictureDTOS);
        houseDTO.setCover(this.cdnPrefix + houseDTO.getCover());





        //完成添加到house_tag
        List<String> tags = houseForm.getTags(); //获得网页中标签的集合
        if (tags != null && !tags.isEmpty()) {
            List<HouseTag> houseTags = new ArrayList<>();
            for (String tag : tags) {
                houseTags.add(new HouseTag(house.getId(), tag));
            }
            houseTagMapper.save(houseTags);
            houseDTO.setTags(tags);
        }

        return ServiceResult.success(null, houseDTO);
    }


    @Override
    /**
     * 修改一个房源
     */
    public ServiceResult update(HouseForm houseForm) {

        House house = this.houseMapper.findById(houseForm.getId());
        if (house == null) {
            return ServiceResult.notFound();
        }

        HouseDetail detail = this.houseDetailMapper.findByHouseId(house.getId());
        if (detail == null) {
            return ServiceResult.notFound();
        }

        //修改house_detail
        ServiceResult wrapperResult = wrapperDetailInfo(detail, houseForm);
        if (wrapperResult != null) {
            return wrapperResult;
        }

        houseDetailMapper.save(detail);

        //修改house_picture
        List<HousePicture> pictures = generatePictures(houseForm, houseForm.getId());
        housePictureMapper.save(pictures);

        //如果封面是null,没有修改,获得之前的封面
        if (houseForm.getCover() == null) {
            houseForm.setCover(house.getCover());
        }

        //修改house
        modelMapper.map(houseForm, house);
        house.setLastUpdateTime(LocalDateTime.now());
        houseMapper.save(house);

        return ServiceResult.success();
    }

    @Override
    public ServiceMultiResult<HouseDTO> adminQuery(DatatableSearch searchBody) {
        return null;
    }

    @Override
    /**
     * 展示一个房源信息
     */
    public ServiceResult<HouseDTO> findCompleteOne(Long id) {

        //查询house
        House house = houseMapper.findById(id);
        if (house == null) {
            return ServiceResult.notFound();
        }

        //查询HouseDetail
        HouseDetail detail = houseDetailMapper.findByHouseId(id);
        List<HousePicture> pictures = housePictureMapper.findAllByHouseId(id);

        //将HouseDetail中的内容存入HouseDetailDTO
        HouseDetailDTO detailDTO = modelMapper.map(detail, HouseDetailDTO.class);
        List<HousePictureDTO> pictureDTOS = new ArrayList<>();
        for (HousePicture picture : pictures) {
            HousePictureDTO pictureDTO = modelMapper.map(picture, HousePictureDTO.class);
            pictureDTOS.add(pictureDTO);
        }

        //查询HouseTag
        List<HouseTag> tags = houseTagMapper.findAllByHouseId(id);
        List<String> tagList = new ArrayList<>();
        for (HouseTag tag : tags) {
            tagList.add(tag.getName());
        }

        //将查询的所有信息放入HouseDTO
        HouseDTO result = modelMapper.map(house, HouseDTO.class);
        result.setHouseDetail(detailDTO);
        result.setPictures(pictureDTOS);
        result.setTags(tagList);

        return ServiceResult.of(result);
    }

    @Override
    /**
     * 从七牛云中删除一个图片
     */
    public ServiceResult removePhoto(Long id) {

        //查询house_picture中id
        HousePicture picture = housePictureMapper.findById(id);

        if (picture == null) {
            return ServiceResult.notFound();
        }

        try {
            //根据id获得house_picture中的路径,删除七牛云的图片
            Response response = this.qiNiuService.delete(picture.getPath());
            if (response.isOK()) {
                //如果七牛云中完成删除,再删除house_picture的信息
                housePictureMapper.deleteById(id);
                return ServiceResult.success();
            } else {
                return new ServiceResult(false, response.error);
            }
        } catch (QiniuException e) {
            e.printStackTrace();
            return new ServiceResult(false, e.getMessage());
        }
    }

    @Override
    /**
     * 修改房源的封面,不需要经过七牛云
     */
    public ServiceResult updateCover(Long coverId, Long targetId) {

        HousePicture cover = housePictureMapper.findById(coverId);
        if (cover == null) {
            return ServiceResult.notFound();
        }

        //获得HousePicture中的图片完成house中封面修改
        houseMapper.updateCover(targetId, cover.getPath());
        return ServiceResult.success();
    }

    @Override
    /**
     * 添加一个房源标签的信息,和添加一个房源中的标签添加是同一个方法
     * 所以使用了集合,但是这个集合只放一个内容,即添加一个标签
     */
    public ServiceResult addTag(Long houseId, String tag) {

        //查询houseId是否存在
        House house = houseMapper.findById(houseId);

        if (house == null) {
            return ServiceResult.notFound();
        }

        //查询要添加的houseTag是否存在
        HouseTag houseTag = houseTagMapper.findByNameAndHouseId(tag, houseId);
        if (houseTag != null) {
            return new ServiceResult(false, "标签已存在");
        }

        //不存在,完成添加
        //此处的添加和添加一个房源中的方法是同一个方法
        List<HouseTag> houseTags = new ArrayList<>();
        houseTags.add(new HouseTag(houseId, tag));
        houseTagMapper.save(houseTags);

        return ServiceResult.success();
    }


    @Override
    /**
     * 删除一个房源的标签信息
     */
    public ServiceResult removeTag(Long houseId, String tag) {

        //查询houseId是否存在
        House house = houseMapper.findById(houseId);
        if (house == null) {
            return ServiceResult.notFound();
        }

        //查询要删除的houseTag是否存在
        HouseTag houseTag = houseTagMapper.findByNameAndHouseId(tag, houseId);
        if (houseTag == null) {
            return new ServiceResult(false, "标签不存在");
        }

        //如果存在,则删除
        houseTagMapper.deleteById(houseTag.getId());
        return ServiceResult.success();
    }

    @Override
    /**
     * 修改房源的状态
     * 未审核,审核通过,已出租,逻辑删除
     */
    public ServiceResult updateStatus(Long id, int status) {

        House house = houseMapper.findById(id);
        if (house == null) {
            return ServiceResult.notFound();
        }

        if (house.getStatus() == status) {
            return new ServiceResult(false, "状态没有发生变化");
        }

        if (house.getStatus() == HouseStatus.RENTED.getValue()) {
            return new ServiceResult(false, "已出租的房源不允许修改状态");
        }

        if (house.getStatus() == HouseStatus.DELETED.getValue()) {
            return new ServiceResult(false, "已删除的资源不允许操作");
        }

        houseMapper.updateStatus(id, status);
        return ServiceResult.success();
    }

    @Override
    public ServiceMultiResult<HouseDTO> query(RentSearch rentSearch) {
        return null;
    }

    /**
     * 获得HouseForm中上传到网页的图片到HousePicture实体的赋值
     * @param form
     * @param houseId
     * @return
     */
    private List<HousePicture> generatePictures(HouseForm form, Long houseId) {
        //房屋图片信息的集合
        List<HousePicture> pictures = new ArrayList<>();
        //判断HouseForm中getPhotos()网页中上传的图片是否为空
        if (form.getPhotos() == null || form.getPhotos().isEmpty()) {
            return pictures;
        }

        //HouseForm中getPhotos()网页中上传的图片不为空,完成赋值
        //遍历HouseForm中getPhotos()集合
        for (PhotoForm photoForm : form.getPhotos()) {
            HousePicture picture = new HousePicture();
            picture.setHouseId(houseId);
            picture.setCdnPrefix(cdnPrefix);
            picture.setPath(photoForm.getPath());
            picture.setWidth(photoForm.getWidth());
            picture.setHeight(photoForm.getHeight());
            pictures.add(picture);
        }
        return pictures;
    }

    /**
     * 完成houseForm属性添加到house_detail
     * @param houseDetail
     * @param houseForm
     * @return
     */
    private ServiceResult<HouseDTO> wrapperDetailInfo(HouseDetail houseDetail, HouseForm houseForm) {

        Subway subway = subwayMapper.findByCityEnName(houseForm.getSubwayLineId());

        if (subway == null) {
            return ServiceResult.of(false, "Not valid subway line!");
        }

        SubwayStation subwayStation = subwayStationMapper.findBySubwayId(houseForm.getSubwayStationId());
        if (subwayStation == null || subway.getId() != subwayStation.getSubwayId()) {
            return ServiceResult.of(false, "Not valid subway station!");
        }

        houseDetail.setSubwayLineId(subway.getId());
        houseDetail.setSubwayLineName(subway.getName());

        houseDetail.setSubwayStationId(subwayStation.getId());
        houseDetail.setSubwayStationName(subwayStation.getName());

        houseDetail.setDescription(houseForm.getDescription());
        houseDetail.setDetailAddress(houseForm.getDetailAddress());
        houseDetail.setLayoutDesc(houseForm.getLayoutDesc());
        houseDetail.setRentWay(houseForm.getRentWay());
        houseDetail.setRoundService(houseForm.getRoundService());
        houseDetail.setTraffic(houseForm.getTraffic());
        return null;

    }


}