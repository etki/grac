package me.etki.grac.infrastructure.dto.github;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class RateLimit {

    private ResourceSection resources;
    private ResourceLimit rate;

    public ResourceSection getResources() {
        return resources;
    }

    public RateLimit setResources(ResourceSection resources) {
        this.resources = resources;
        return this;
    }

    public ResourceLimit getRate() {
        return rate;
    }

    public RateLimit setRate(ResourceLimit rate) {
        this.rate = rate;
        return this;
    }

    public static class ResourceLimit {

        private Integer limit;
        private Integer remaining;
        private Integer reset;

        public Integer getLimit() {
            return limit;
        }

        public ResourceLimit setLimit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public Integer getRemaining() {
            return remaining;
        }

        public ResourceLimit setRemaining(Integer remaining) {
            this.remaining = remaining;
            return this;
        }

        public Integer getReset() {
            return reset;
        }

        public ResourceLimit setReset(Integer reset) {
            this.reset = reset;
            return this;
        }
    }

    public static class ResourceSection {

        private ResourceLimit core;
        private ResourceLimit search;

        public ResourceLimit getCore() {
            return core;
        }

        public ResourceSection setCore(ResourceLimit core) {
            this.core = core;
            return this;
        }

        public ResourceLimit getSearch() {
            return search;
        }

        public ResourceSection setSearch(ResourceLimit search) {
            this.search = search;
            return this;
        }
    }
}
