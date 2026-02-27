<template>
  <div class="foot">
    <p>&copy; 2024 TradingSimulator, All rights reserved.</p>
  </div>
</template>

<style scoped>
.foot {
  width: 100%;
  background-color: transparent;
  border-top: 1px solid var(--border-glass);
  height: 60px;
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
  margin-top: auto;
}

p {
  color: var(--text-muted);
  font-size: 0.95rem;
  margin: 0;
}

@media screen and (max-width: 515px) {
  p {
    font-size: 0.85rem;
  }
}
</style>
