<template>
  <div class="container">
    <div class="headline">
      <h1>MARKET NEWS</h1>
    </div>
    <div class="search-news">
      <button>Search Key</button>
      <input type="text" placeholder="Search for news" v-model="searchInput" />
    </div>
    <div class="news-card-container" v-if="!isLoading">
      <div class="news-card" v-for="card in filteredList" :key="card.id">
        <div class="image">
          <img :src="card.image" />
        </div>
        <div class="card-info">
          <div class="source-date">
            <div class="source">
              <p>{{ card.source }}</p>
            </div>
            <div class="date">
              <p>{{unixToDate(card.datetime)}}</p>
            </div>
          </div>
          <div class="card-headline">
            <h2>{{ card.headline }}</h2>
          </div>
          <div class="description">
            <p>{{ card.summary }}</p>
          </div>
          <div class="button">
            <button><a :href="card.url" target="_blank">Read More</a></button>
          </div>
        </div>
      </div>
    </div>
    <div class="loading" v-else>
      <loading-spinner id="spinner" :spin="isLoading" />
    </div>
  </div>
</template>

<script>
import newsService from "../services/NewsService";
import LoadingSpinner from "./LoadingSpinner.vue";
export default {
  data() {
    return {
      news: [],
      isLoading: false,
      searchInput: "",
    };
  },
  created() {
    this.listNews();
  },
  methods: {
    listNews() {
      this.isLoading = true;
      newsService
        .getNews()
        .then((response) => {
          if (response.status == 200) {
            this.news = response.data;
            this.isLoading = false;
          }
        })
        .catch((error) => {
          console.error("Error occurred retrieving news.", error);
        });
    },
    unixToDate(time){
      const date = new Date(time  * 1000);
      return date;
    }
  },
  components: {
    LoadingSpinner,
  },
  computed: {
    filteredList() {
      let list = this.news;
      if (this.searchInput != "") {
        list = list.filter((news) => {
          return (
            news.headline.toLowerCase().includes(this.searchInput.toLowerCase()) ||
            news.source.toLowerCase().includes(this.searchInput.toLowerCase()) ||
            news.datetime == this.searchInput ||
            news.summary.toLowerCase().includes(this.searchInput.toLowerCase())
          );
        });
      }
      return list;
    },
  },
};
</script>

<style scoped>
.container {
  min-height: 100vh;
  width: 90%;
  margin: 10px auto;
  display: flex;
  flex-direction: column;
  padding: 50px;
}
.headline {
  margin-bottom: 10px;
}
h1 {
  font-size: 38px;
  text-align: center;
  border-bottom: 3px solid #bbbbbb;
  padding-bottom: 10px;
}
.search-news {
  display: flex;
  justify-content: space-between;
  margin-bottom: 35px;
  align-items: center;
}

.search-news input {
  width: 100%;
  flex: 1;
  border-radius: 0 5px 5px 0;
  padding: 2px;
  text-align: center;
  border: 1px solid rgb(205, 205, 205);
  outline: none;
}
.search-news button {
  background-color: black;
  padding: 2px;
  max-width: 180px;
  color: white;
  border-radius: 5px 0 0 5px;
  border: none;
}


.news-card-container {
  display: flex;
  flex-direction: column;
}

.news-card {
  display: flex;
  gap: 30px;
  margin-bottom: 50px;
  border: 1px solid #bbbbbb;
  border-radius: 10px;
  padding: 20px;
  overflow: hidden;
}

.news-card:hover {
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
}

.image {
  width: 30%;
}
.image img {
  max-width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 10px;
}

.card-info {
  width: 90%;
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  gap: 12px;
}

.source-date {
  display: flex;
  gap: 10px;
}

.source-date p {
  color: #898989;
  font-weight: 400;
}

.source {
  border-right: 1px solid rgb(205, 205, 205);
}

.source p {
  margin-right: 10px;
}

h2 {
  font-size: 20px;
}

button {
  background-color: black;
  padding: 5px 20px;
  border-radius: 10px;
  border: none;
}

button a {
  text-decoration: none;
  color: white;
}

.loading {
  height: 40vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

@media screen and (max-width: 800px) {
  .search-news button {
   
    overflow: hidden;
  }
  .news-card {
    display: flex;
    flex-direction: column;
  }

  .image {
    width: 100%;
  }
}
</style>
