import axios from 'axios';

export default {

  getCurrentProfile(){
    return axios.get(`/api/profiles`);
  },

  adminGetAllProfiles(){
    return axios.get(`/api/profiles/admin`);
  },

  adminGetProfileById(id){
    return axios.get(`/api/profiles/admin/${id}`);
  },

  createProfile(profile){
    return axios.post(`/api/profiles`, profile);
  },

  updateProfile(id, profile){
    return axios.put(`/api/profiles/${id}`, profile);
  },
  adminDeleteProfile(id){
    return axios.delete(`/api/profiles/admin/${id}`);
  }

}