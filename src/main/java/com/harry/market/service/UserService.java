package com.harry.market.service;

import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harry.market.common.Constants;
import com.harry.market.controller.UserController;
import com.harry.market.controller.dto.*;
import com.harry.market.controller.vo.AuditUserVO;
import com.harry.market.controller.vo.UserInfoVO;
import com.harry.market.controller.vo.UserVO;
import com.harry.market.entity.*;
import com.harry.market.exception.ServiceException;
import com.harry.market.mapper.*;
import com.harry.market.utils.ExcelUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @authors HarryGao、222100209_李炎东
 */
@Service
public class UserService extends ServiceImpl<UserMapper,User> {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserDetailsMapper userDetailsMapper;
    @Resource
    private BuyerMsgMapper buyerMsgMapper;
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private UserOrderMapper userOrderMapper;
    @Resource
    private OrderMapper orderMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserController userController;
    @Autowired
    private BCryptPasswordEncoder encoder;

    private static final Log LOG = Log.get();

    /**
     * @author HarryGao
     * @param userDTO
     * @return
     */
    public UserDTO login(UserDTO userDTO) {
        // 改成强Hash加密了 by lyd
//        userDTO.setPassword(Md5Utils.code(userDTO.getPassword()));
        String password = getUserPwd(userDTO.getUsername());
        if (password != null && encoder.matches(userDTO.getPassword(),password)) {
            userDTO.setPassword(password);
            return userDTO;
        } else {
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }
    }

    /**
     * @author HarryGao
     * @param userDTO
     * @return
     */
    public UserVO getUserInfo(UserDTO userDTO){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userDTO.getUsername());
        queryWrapper.eq("password",userDTO.getPassword());
        User one;
        try{
            one=getOne(queryWrapper); //从数据库查询用户信息
        }catch(Exception e){
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        UserVO userVO = new UserVO();
        userVO.setId(one.getId().toString());
        userVO.setUsername(one.getUsername());
        userVO.setPassword(one.getPassword());
        userVO.setPerm(one.getPerm());
        userVO.set_deleted(one.is_deleted());
        userVO.setGmt_modified(one.getGmt_modified());
        userVO.setGmt_modified(one.getGmt_create());
        return userVO;
    }

    /**
     * @author HarryGao
     * @param loginName
     * @return
     */
    private String getUserPwd(String loginName){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",loginName);
        String password;
        try{
            password=getOne(queryWrapper).getPassword(); //从数据库查询用户信息
        }catch(Exception e){
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        return password;
    }

    /**
     * @author HarryGao
     * @param username
     * @return
     */
    public String getUserPerm(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        String perm = userMapper.selectOne(wrapper).getPerm();
        return perm;
    }

    /**
     * @author HarryGao
     * @param username
     * @return
     */
    public User getUser(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        return userMapper.selectOne(wrapper);
    }

    /**
     * @author HarryGao
     * @param username
     * @return
     */
    public Long getUserId(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        return userMapper.selectOne(wrapper).getId();
    }

    /**
     * @author 222100209_李炎东
     * @usage 从表格中读取用户信息，并导入数据库
     * @param row 从几行开始输入
     * @param num 输入几条数据
     */
    public void insertExcelUser(int row,int num) {
        ExcelUtill data = new ExcelUtill("D:\\Code\\project\\coporation\\data.xlsx", "data");
        List<UserDetails> users = data.getExcelUser(row, num);
        for (UserDetails user : users) {
            UserRegDTO userRegDTO = new UserRegDTO();
            userRegDTO.setUsername(user.getUsername());
            userRegDTO.setPassword("123456");
            userRegDTO.setEmail(user.getEmail());
            userController.register(userRegDTO);
            user.setId(userService.getUserId(userRegDTO.getUsername()));
            userDetailsMapper.updateById(user);
        }

    }

//    public UserInfoDTO getUserInfo() {
//        String username;
//        //从springsecurity中取出当前用户
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof UserDetails) {
//            username = ((UserDetails)principal).getUsername();
//        } else {
//            username = principal.toString();
//        }
//
//        return userDetailsMapper.getUserInfo(username);
//    }

    /**
     * @author 222100209_李炎东
     * @usage 通过用户Id查询并返回给前端
     * @param id 用户Id
     * @return
     */
    public UserInfoVO getUserInfoById(Long id) {
        UserInfoVO info;
        info = userDetailsMapper.getUserInfo(id);
        if ("0".equals(info.getGender())) {
            info.setGender("不详");
        } else if ("1".equals(info.getGender())) {
            info.setGender("男");
        } else if ("2".equals(info.getGender())) {
            info.setGender("女");
        }
        return info;
    }

    /**
     * @author 222100209_李炎东
     * @usage 修改密码
     * @param chgPwdDTO
     */
    public void changePassword(ChgPwdDTO chgPwdDTO) {
        String password = chgPwdDTO.getNewPassword();
        password = encoder.encode(password);
        chgPwdDTO.setNewPassword(password);
        User user = new User();
        user.setId(getUserbyEmail(chgPwdDTO.getEmail()).getId());
        user.setPassword(chgPwdDTO.getNewPassword());
        userService.updateById(user);
    }

    /**
     * @author 222100209_李炎东
     * @param id 用户Id
     * @return
     */
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    /**
     * @author 222100209_李炎东
     * @usage 修改用户信息
     * @param chgUserInfoDTO
     */
    public void changeUserInfo(ChgUserInfoDTO chgUserInfoDTO) {
        UserDetails userDetails = new UserDetails();
        userDetails.setId(chgUserInfoDTO.getUserId());
        userDetails.setRealname(chgUserInfoDTO.getRealname());
        userDetails.setNickname(chgUserInfoDTO.getNickname());
        userDetails.setGender(chgUserInfoDTO.getGender());
        userDetails.setAge(chgUserInfoDTO.getAge());
        userDetails.setAddress(chgUserInfoDTO.getAddress());
        userDetails.setTel(chgUserInfoDTO.getTel());
        userDetails.setEmail(chgUserInfoDTO.getEmail());
        userDetails.setQq(chgUserInfoDTO.getQq());
        userDetails.setIntroduction(chgUserInfoDTO.getIntro());

        BuyerMsg buyerMsg = new BuyerMsg();
        buyerMsg.setId(chgUserInfoDTO.getUserId());
        buyerMsg.setNickname(chgUserInfoDTO.getNickname());
        buyerMsg.setTel(chgUserInfoDTO.getTel());
        buyerMsg.setAddress(chgUserInfoDTO.getAddress());

        userDetailsMapper.updateById(userDetails);
        buyerMsgMapper.updateById(buyerMsg);
    }

    /**
     * @author 222100209_李炎东
     * @usage 通过邮箱获取用户信息
     * @param email
     * @return
     */
    public UserDetails getUserbyEmail(String email) {
        QueryWrapper<UserDetails> wrapper = new QueryWrapper<>();
        wrapper.eq("email",email);
        return userDetailsMapper.selectList(wrapper).get(0);

    }

    /**
     * @author 222100209_李炎东
     * @usage 逻辑删除用户
     * @param userId
     */
    public void delUser(Long userId) {
        QueryWrapper<Item> itemWrapper = new QueryWrapper<>();
        itemWrapper.eq("seller_id",userId);
        goodsMapper.delete(itemWrapper);

        QueryWrapper<UserOrder> userOrderWrapper = new QueryWrapper<>();
        userOrderWrapper.eq("buyer_id",userId);
        List<UserOrder> userOrders = userOrderMapper.selectList(userOrderWrapper);
        for (UserOrder uo : userOrders) {
            orderMapper.deleteById(uo.getId());
        }

        userOrderMapper.delete(userOrderWrapper);
        buyerMsgMapper.deleteById(userId);
        userDetailsMapper.deleteById(userId);
        userMapper.deleteById(userId);
    }

    /**
     * @author 222100209_李炎东
     * @param pageNum 第?页
     * @param pageSize 一页?条数据
     * @param sort 筛选参数
     * @param order 排序
     * @return
     */
    public List<AuditUserVO> getUser(Integer pageNum,Integer pageSize,String sort,String order) {
        List<AuditUserVO> allUser = userMapper.getAllUser(pageNum*pageSize, pageSize, sort, order);
        ArrayList<AuditUserVO> res = new ArrayList<>();
        for (AuditUserVO vo : allUser) {
            AuditUserVO r = new AuditUserVO();
            r = vo;
            if (vo.is_deleted()) {
                r.setStatus("已封禁");
            } else {
                r.setStatus("正常");
            }
            res.add(r);
        }

        return res;
    }

    /**
     * @author 222100209_李炎东
     * @usage 获取所有用户数量
     * @return
     */
    public Long getUserCount() {
        return userMapper.getUserCount();
    }

    /**
     * @author 222100209_李炎东
     * @usage 模糊查询用户
     * @param nname 查询参数
     * @param pageNum 第?页
     * @param pageSize 一页?条数据
     * @return
     */
    public List<AuditUserVO> searchUser(String nname,Integer pageNum, Integer pageSize) {
        QueryWrapper<UserDetails> wrapper = new QueryWrapper<>();
        wrapper.like("nickname",nname);
        wrapper.last("limit "+pageNum*pageSize+","+pageSize);
        List<UserDetails> users = userDetailsMapper.selectList(wrapper);
        ArrayList<AuditUserVO> res = new ArrayList<>();
        for (UserDetails user : users) {
            AuditUserVO vo = new AuditUserVO();
            vo.setUserId(user.getId().toString());
            QueryWrapper<Item> itemWrapper = new QueryWrapper<>();
            itemWrapper.eq("seller_id",user.getId());
            Long released = goodsMapper.selectCount(itemWrapper);
            itemWrapper.eq("is_audit",1);
            Long unpassed = goodsMapper.selectCount(itemWrapper);
            vo.setUsername(user.getNickname());
            vo.setEmail(user.getEmail());
            vo.setHead(user.getHead());
            vo.setReleasedNum(released);
            vo.setUnpassedNum(unpassed);
            res.add(vo);
        }
        return res;
    }

    /**
     * @author 222100209_李炎东
     * @usgae 获取查询到的结果数量
     * @param nName
     * @return
     */
    public Long searchUserCount(String nName) {
        QueryWrapper<UserDetails> wrapper = new QueryWrapper<>();
        wrapper.like("nickname",nName);
        return userDetailsMapper.selectCount(wrapper);
    }

}
