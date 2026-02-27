<template>
  <div class="news-wrapper">
    <!-- Headline -->
    <div class="headline">
      <h1 class="title-primary">Market News</h1>
    </div>

    <!-- Search -->
    <div class="search-section">
      <div class="search-input-wrapper">
        <i class="bx bx-search search-icon"></i>
        <input
          type="text"
          placeholder="Search for news..."
          v-model="searchInput"
          class="search-input"
        />
      </div>
    </div>

    <!-- News Cards -->
    <div class="news-list" v-if="!isLoading">
      <div
        class="news-card glass-panel"
        v-for="card in filteredList"
        :key="card.id"
      >
        <div class="news-image-container">
          <img :src="card.image || defaultImage" class="news-image" />
        </div>
        <div class="news-content">
          <div class="news-meta">
            <span class="news-source">{{ card.source }}</span>
            <span class="news-date">{{ formatDate(card.datetime) }}</span>
          </div>
          <h2 class="news-headline">{{ card.headline }}</h2>
          <p class="news-summary">{{ card.summary }}</p>
          <a :href="card.url" target="_blank" class="read-more-btn">
            Read More <i class="bx bx-right-arrow-alt"></i>
          </a>
        </div>
      </div>
    </div>

    <!-- Loading -->
    <div class="loading-state" v-else>
      <loading-spinner id="spinner" :spin="isLoading" />
    </div>
  </div>
</template>

<script>
import newsService from "../services/NewsService";
import LoadingSpinner from "./LoadingSpinner.vue";

export default {
  components: { LoadingSpinner },
  data() {
    return {
      news: [],
      isLoading: false,
      searchInput: "",
      defaultImage:
        "https://www.shutterstock.com/image-vector/default-ui-image-placeholder-wireframes-600nw-1037719192.jpg",
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
          if (response.status === 200) {
            this.news = response.data;
          }
        })
        .catch((err) => console.error("Error fetching news:", err))
        .finally(() => {
          this.isLoading = false;
        });
    },
    formatDate(unixTime) {
      const date = new Date(unixTime * 1000);
      return date.toLocaleString(); // e.g., "12/11/2025, 2:30 PM"
    },
  },
  computed: {
    filteredList() {
      if (!this.searchInput) return this.news;
      return this.news.filter(
        (n) =>
          n.headline.toLowerCase().includes(this.searchInput.toLowerCase()) ||
          n.source.toLowerCase().includes(this.searchInput.toLowerCase()) ||
          n.summary.toLowerCase().includes(this.searchInput.toLowerCase()),
      );
    },
  },
};
</script>

<style scoped>
.news-wrapper {
  padding: 40px 24px;
  max-width: 1200px;
  margin: 0 auto;
  min-height: 80vh;
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.headline {
  text-align: center;
  margin-bottom: 10px;
}

.title-primary {
  font-size: 2.5rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
  letter-spacing: -0.02em;
}

/* Search Box */
.search-section {
  display: flex;
  justify-content: center;
  gap: 12px;
  max-width: 600px;
  margin: 0 auto;
  width: 100%;
}

.search-input-wrapper {
  position: relative;
  flex: 1;
  display: flex;
  align-items: center;
}

.search-icon {
  position: absolute;
  left: 16px;
  color: var(--text-muted);
  font-size: 1.2rem;
}

.search-input {
  width: 100%;
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid var(--border-glass);
  border-radius: var(--radius-md);
  padding: 14px 16px 14px 44px;
  color: var(--text-primary);
  font-size: 1rem;
  outline: none;
  transition: var(--transition-smooth);
}

.search-input:focus {
  border-color: var(--accent-primary);
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
  background: rgba(255, 255, 255, 0.05);
}

.btn-primary {
  background: var(--accent-primary);
  color: #fff;
  border: none;
  border-radius: var(--radius-md);
  padding: 0 24px;
  font-weight: 600;
  cursor: pointer;
  transition: var(--transition-smooth);
}

.btn-primary:hover {
  background: #2563eb;
  transform: translateY(-2px);
  box-shadow: var(--shadow-glow);
}

/* News List */
.news-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* Card */
.news-card {
  display: flex;
  padding: 0;
  overflow: hidden;
  transition: var(--transition-smooth);
  border: 1px solid var(--border-glass);
}

.news-card:hover {
  transform: translateY(-4px);
  border-color: rgba(255, 255, 255, 0.15);
  box-shadow: var(--shadow-glow);
}

.news-image-container {
  width: 320px;
  flex-shrink: 0;
}

.news-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.news-content {
  padding: 24px;
  display: flex;
  flex-direction: column;
  flex: 1;
}

.news-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.news-source {
  font-size: 0.85rem;
  font-weight: 600;
  color: var(--accent-primary);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.news-date {
  font-size: 0.85rem;
  color: var(--text-muted);
}

.news-headline {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 12px 0;
  line-height: 1.4;
}

.news-summary {
  font-size: 0.95rem;
  color: var(--text-secondary);
  line-height: 1.6;
  margin: 0 0 20px 0;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.read-more-btn {
  margin-top: auto;
  align-self: flex-start;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 0.95rem;
  font-weight: 500;
  color: var(--text-primary);
  text-decoration: none;
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid var(--border-glass);
  border-radius: var(--radius-full);
  transition: var(--transition-smooth);
}

.read-more-btn:hover {
  background: var(--accent-primary);
  border-color: var(--accent-primary);
  color: #fff;
  transform: translateX(4px);
}

.loading-state {
  display: flex;
  justify-content: center;
  padding: 60px 0;
}

@media (max-width: 768px) {
  .news-card {
    flex-direction: column;
  }

  .news-image-container {
    width: 100%;
    height: 200px;
  }
}
</style>
