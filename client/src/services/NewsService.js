import axios from 'axios'

export default{
    getNews(){
        return axios.get(`/api/news`);
    }
}