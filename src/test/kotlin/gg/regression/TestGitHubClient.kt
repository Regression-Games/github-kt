import gg.regression.getMockedClient
import gg.regression.github.GitHubClient
import gg.regression.github.exceptions.UnauthorizedException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class TestGitHubClient : FeatureSpec({

    feature("user profile access") {

        scenario("should retrieve the user when authed") {
            val exampleResponse = """
                {
                  "login": "octocat",
                  "id": 1,
                  "node_id": "MDQ6VXNlcjE=",
                  "avatar_url": "https://github.com/images/error/octocat_happy.gif",
                  "gravatar_id": "",
                  "url": "https://api.github.com/users/octocat",
                  "html_url": "https://github.com/octocat",
                  "followers_url": "https://api.github.com/users/octocat/followers",
                  "following_url": "https://api.github.com/users/octocat/following{/other_user}",
                  "gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
                  "starred_url": "https://api.github.com/users/octocat/starred{/owner}{/repo}",
                  "subscriptions_url": "https://api.github.com/users/octocat/subscriptions",
                  "organizations_url": "https://api.github.com/users/octocat/orgs",
                  "repos_url": "https://api.github.com/users/octocat/repos",
                  "events_url": "https://api.github.com/users/octocat/events{/privacy}",
                  "received_events_url": "https://api.github.com/users/octocat/received_events",
                  "type": "User",
                  "site_admin": false,
                  "name": "monalisa octocat",
                  "company": "GitHub",
                  "blog": "https://github.com/blog",
                  "location": "San Francisco",
                  "email": "octocat@github.com",
                  "hireable": false,
                  "bio": "There once was...",
                  "twitter_username": "monatheoctocat",
                  "public_repos": 2,
                  "public_gists": 1,
                  "followers": 20,
                  "following": 0,
                  "created_at": "2008-01-14T04:33:35Z",
                  "updated_at": "2008-01-14T04:33:35Z",
                  "private_gists": 81,
                  "total_private_repos": 100,
                  "owned_private_repos": 100,
                  "disk_usage": 10000,
                  "collaborators": 8,
                  "two_factor_authentication": true,
                  "plan": {
                    "name": "Medium",
                    "space": 400,
                    "private_repos": 20,
                    "collaborators": 0
                  }
                }
            """.trimIndent()
            val client = GitHubClient("oauth_code_here")
            client.httpClient = getMockedClient(500, exampleResponse)
            val user = client.getLoggedInUser()
            user.login shouldBe "octocat"
            user.name shouldBe "monalisa octocat"
            user.plan?.privateRepos shouldBe 20
        }

        scenario("should throw an error when unauthorized") {
            val exampleResponse = """
                {
                    "message": "Bad credentials",
                    "documentation_url": "https://docs.github.com/rest"
                }
            """.trimIndent()
            val client = GitHubClient("oauth_code_here")
            client.httpClient = getMockedClient(401, exampleResponse)
            shouldThrow<UnauthorizedException> {
                client.getLoggedInUser()
            }
        }
    }

    feature("getting repositories") {

        scenario("should return a list of repositories") {
            val exampleResponse = """
                [
                  {
                    "id": 1296269,
                    "node_id": "MDEwOlJlcG9zaXRvcnkxMjk2MjY5",
                    "name": "Hello-World",
                    "full_name": "octocat/Hello-World",
                    "owner": {
                      "login": "octocat",
                      "id": 1,
                      "node_id": "MDQ6VXNlcjE=",
                      "avatar_url": "https://github.com/images/error/octocat_happy.gif",
                      "gravatar_id": "",
                      "url": "https://api.github.com/users/octocat",
                      "html_url": "https://github.com/octocat",
                      "followers_url": "https://api.github.com/users/octocat/followers",
                      "following_url": "https://api.github.com/users/octocat/following{/other_user}",
                      "gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
                      "starred_url": "https://api.github.com/users/octocat/starred{/owner}{/repo}",
                      "subscriptions_url": "https://api.github.com/users/octocat/subscriptions",
                      "organizations_url": "https://api.github.com/users/octocat/orgs",
                      "repos_url": "https://api.github.com/users/octocat/repos",
                      "events_url": "https://api.github.com/users/octocat/events{/privacy}",
                      "received_events_url": "https://api.github.com/users/octocat/received_events",
                      "type": "User",
                      "site_admin": false
                    },
                    "private": false,
                    "html_url": "https://github.com/octocat/Hello-World",
                    "description": "This your first repo!",
                    "fork": false,
                    "url": "https://api.github.com/repos/octocat/Hello-World",
                    "archive_url": "https://api.github.com/repos/octocat/Hello-World/{archive_format}{/ref}",
                    "assignees_url": "https://api.github.com/repos/octocat/Hello-World/assignees{/user}",
                    "blobs_url": "https://api.github.com/repos/octocat/Hello-World/git/blobs{/sha}",
                    "branches_url": "https://api.github.com/repos/octocat/Hello-World/branches{/branch}",
                    "collaborators_url": "https://api.github.com/repos/octocat/Hello-World/collaborators{/collaborator}",
                    "comments_url": "https://api.github.com/repos/octocat/Hello-World/comments{/number}",
                    "commits_url": "https://api.github.com/repos/octocat/Hello-World/commits{/sha}",
                    "compare_url": "https://api.github.com/repos/octocat/Hello-World/compare/{base}...{head}",
                    "contents_url": "https://api.github.com/repos/octocat/Hello-World/contents/{+path}",
                    "contributors_url": "https://api.github.com/repos/octocat/Hello-World/contributors",
                    "deployments_url": "https://api.github.com/repos/octocat/Hello-World/deployments",
                    "downloads_url": "https://api.github.com/repos/octocat/Hello-World/downloads",
                    "events_url": "https://api.github.com/repos/octocat/Hello-World/events",
                    "forks_url": "https://api.github.com/repos/octocat/Hello-World/forks",
                    "git_commits_url": "https://api.github.com/repos/octocat/Hello-World/git/commits{/sha}",
                    "git_refs_url": "https://api.github.com/repos/octocat/Hello-World/git/refs{/sha}",
                    "git_tags_url": "https://api.github.com/repos/octocat/Hello-World/git/tags{/sha}",
                    "git_url": "git:github.com/octocat/Hello-World.git",
                    "issue_comment_url": "https://api.github.com/repos/octocat/Hello-World/issues/comments{/number}",
                    "issue_events_url": "https://api.github.com/repos/octocat/Hello-World/issues/events{/number}",
                    "issues_url": "https://api.github.com/repos/octocat/Hello-World/issues{/number}",
                    "keys_url": "https://api.github.com/repos/octocat/Hello-World/keys{/key_id}",
                    "labels_url": "https://api.github.com/repos/octocat/Hello-World/labels{/name}",
                    "languages_url": "https://api.github.com/repos/octocat/Hello-World/languages",
                    "merges_url": "https://api.github.com/repos/octocat/Hello-World/merges",
                    "milestones_url": "https://api.github.com/repos/octocat/Hello-World/milestones{/number}",
                    "notifications_url": "https://api.github.com/repos/octocat/Hello-World/notifications{?since,all,participating}",
                    "pulls_url": "https://api.github.com/repos/octocat/Hello-World/pulls{/number}",
                    "releases_url": "https://api.github.com/repos/octocat/Hello-World/releases{/id}",
                    "ssh_url": "git@github.com:octocat/Hello-World.git",
                    "stargazers_url": "https://api.github.com/repos/octocat/Hello-World/stargazers",
                    "statuses_url": "https://api.github.com/repos/octocat/Hello-World/statuses/{sha}",
                    "subscribers_url": "https://api.github.com/repos/octocat/Hello-World/subscribers",
                    "subscription_url": "https://api.github.com/repos/octocat/Hello-World/subscription",
                    "tags_url": "https://api.github.com/repos/octocat/Hello-World/tags",
                    "teams_url": "https://api.github.com/repos/octocat/Hello-World/teams",
                    "trees_url": "https://api.github.com/repos/octocat/Hello-World/git/trees{/sha}",
                    "clone_url": "https://github.com/octocat/Hello-World.git",
                    "mirror_url": "git:git.example.com/octocat/Hello-World",
                    "hooks_url": "https://api.github.com/repos/octocat/Hello-World/hooks",
                    "svn_url": "https://svn.github.com/octocat/Hello-World",
                    "homepage": "https://github.com",
                    "language": null,
                    "forks_count": 9,
                    "stargazers_count": 80,
                    "watchers_count": 80,
                    "size": 108,
                    "default_branch": "master",
                    "open_issues_count": 0,
                    "is_template": true,
                    "topics": [
                      "octocat",
                      "atom",
                      "electron",
                      "api"
                    ],
                    "has_issues": true,
                    "has_projects": true,
                    "has_wiki": true,
                    "has_pages": false,
                    "has_downloads": true,
                    "archived": false,
                    "disabled": false,
                    "visibility": "public",
                    "pushed_at": "2011-01-26T19:06:43Z",
                    "created_at": "2011-01-26T19:01:12Z",
                    "updated_at": "2011-01-26T19:14:43Z",
                    "permissions": {
                      "admin": false,
                      "push": false,
                      "pull": true
                    },
                    "allow_rebase_merge": true,
                    "template_repository": null,
                    "temp_clone_token": "ABTLWHOULUVAXGTRYU7OC2876QJ2O",
                    "allow_squash_merge": true,
                    "allow_auto_merge": false,
                    "delete_branch_on_merge": true,
                    "allow_merge_commit": true,
                    "subscribers_count": 42,
                    "network_count": 0,
                    "license": {
                      "key": "mit",
                      "name": "MIT License",
                      "url": "https://api.github.com/licenses/mit",
                      "spdx_id": "MIT",
                      "node_id": "MDc6TGljZW5zZW1pdA==",
                      "html_url": "https://github.com/licenses/mit"
                    },
                    "forks": 1,
                    "open_issues": 1,
                    "watchers": 1
                  }
                ]
            """.trimIndent()
            val client = GitHubClient("oauth_code_here")
            client.httpClient = getMockedClient(200, exampleResponse)
            val repos = client.getRepositories()
            repos shouldHaveSize 1
            repos[0].fullName shouldBe "octocat/Hello-World"
        }

        scenario("should handle an empty list of repositories") {
            val exampleResponse = "[]"
            val client = GitHubClient("oauth_code_here")
            client.httpClient = getMockedClient(200, exampleResponse)
            val repos = client.getRepositories()
            repos shouldHaveSize 0
        }

        scenario("should fail when not authenticated") {
            val client = GitHubClient("oauth_code_here")
            client.httpClient = getMockedClient(401, "")
            shouldThrow<UnauthorizedException> {
                client.getRepositories()
            }
        }
    }

    feature("getting branches of a repo") {

        scenario("should correctly retrieve branches") {
            val exampleResponse = """
                [
                  {
                    "name": "master",
                    "commit": {
                      "sha": "c5b97d5ae6c19d5c5df71a34c7fbeeda2479ccbc",
                      "url": "https://api.github.com/repos/octocat/Hello-World/commits/c5b97d5ae6c19d5c5df71a34c7fbeeda2479ccbc"
                    },
                    "protected": true,
                    "protection": {
                      "required_status_checks": {
                        "enforcement_level": "non_admins",
                        "contexts": [
                          "ci-test",
                          "linter"
                        ]
                      }
                    },
                    "protection_url": "https://api.github.com/repos/octocat/hello-world/branches/master/protection"
                  }
                ]
            """.trimIndent()
            val client = GitHubClient("oauth_code_here")
            client.httpClient = getMockedClient(200, exampleResponse)
            val branches = client.getBranches("RegressionGames", "client")
            branches shouldHaveSize 1
            branches[0].name shouldBe "master"
            branches[0].commit.sha shouldBe "c5b97d5ae6c19d5c5df71a34c7fbeeda2479ccbc"
        }
    }

    feature("getting commits of a branch") {

        scenario("should correctly retrieve commits") {
            val exampleResponse = """
                [
                  {
                    "url": "https://api.github.com/repos/octocat/Hello-World/commits/6dcb09b5b57875f334f61aebed695e2e4193db5e",
                    "sha": "6dcb09b5b57875f334f61aebed695e2e4193db5e",
                    "node_id": "MDY6Q29tbWl0NmRjYjA5YjViNTc4NzVmMzM0ZjYxYWViZWQ2OTVlMmU0MTkzZGI1ZQ==",
                    "html_url": "https://github.com/octocat/Hello-World/commit/6dcb09b5b57875f334f61aebed695e2e4193db5e",
                    "comments_url": "https://api.github.com/repos/octocat/Hello-World/commits/6dcb09b5b57875f334f61aebed695e2e4193db5e/comments",
                    "commit": {
                      "url": "https://api.github.com/repos/octocat/Hello-World/git/commits/6dcb09b5b57875f334f61aebed695e2e4193db5e",
                      "author": {
                        "name": "Monalisa Octocat",
                        "email": "support@github.com",
                        "date": "2011-04-14T16:00:49Z"
                      },
                      "committer": {
                        "name": "Monalisa Octocat",
                        "email": "support@github.com",
                        "date": "2011-04-14T16:00:49Z"
                      },
                      "message": "Fix all the bugs",
                      "tree": {
                        "url": "https://api.github.com/repos/octocat/Hello-World/tree/6dcb09b5b57875f334f61aebed695e2e4193db5e",
                        "sha": "6dcb09b5b57875f334f61aebed695e2e4193db5e"
                      },
                      "comment_count": 0,
                      "verification": {
                        "verified": false,
                        "reason": "unsigned",
                        "signature": null,
                        "payload": null
                      }
                    },
                    "author": {
                      "login": "octocat",
                      "id": 1,
                      "node_id": "MDQ6VXNlcjE=",
                      "avatar_url": "https://github.com/images/error/octocat_happy.gif",
                      "gravatar_id": "",
                      "url": "https://api.github.com/users/octocat",
                      "html_url": "https://github.com/octocat",
                      "followers_url": "https://api.github.com/users/octocat/followers",
                      "following_url": "https://api.github.com/users/octocat/following{/other_user}",
                      "gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
                      "starred_url": "https://api.github.com/users/octocat/starred{/owner}{/repo}",
                      "subscriptions_url": "https://api.github.com/users/octocat/subscriptions",
                      "organizations_url": "https://api.github.com/users/octocat/orgs",
                      "repos_url": "https://api.github.com/users/octocat/repos",
                      "events_url": "https://api.github.com/users/octocat/events{/privacy}",
                      "received_events_url": "https://api.github.com/users/octocat/received_events",
                      "type": "User",
                      "site_admin": false
                    },
                    "committer": {
                      "login": "octocat",
                      "id": 1,
                      "node_id": "MDQ6VXNlcjE=",
                      "avatar_url": "https://github.com/images/error/octocat_happy.gif",
                      "gravatar_id": "",
                      "url": "https://api.github.com/users/octocat",
                      "html_url": "https://github.com/octocat",
                      "followers_url": "https://api.github.com/users/octocat/followers",
                      "following_url": "https://api.github.com/users/octocat/following{/other_user}",
                      "gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
                      "starred_url": "https://api.github.com/users/octocat/starred{/owner}{/repo}",
                      "subscriptions_url": "https://api.github.com/users/octocat/subscriptions",
                      "organizations_url": "https://api.github.com/users/octocat/orgs",
                      "repos_url": "https://api.github.com/users/octocat/repos",
                      "events_url": "https://api.github.com/users/octocat/events{/privacy}",
                      "received_events_url": "https://api.github.com/users/octocat/received_events",
                      "type": "User",
                      "site_admin": false
                    },
                    "parents": [
                      {
                        "url": "https://api.github.com/repos/octocat/Hello-World/commits/6dcb09b5b57875f334f61aebed695e2e4193db5e",
                        "sha": "6dcb09b5b57875f334f61aebed695e2e4193db5e"
                      }
                    ]
                  }
                ]
            """.trimIndent()
            val client = GitHubClient("oauth_code_here")
            client.httpClient = getMockedClient(200, exampleResponse)
            val commits = client.getCommits("RegressionGames", "client")
            commits shouldHaveSize 1
            commits[0].commit.message shouldBe "Fix all the bugs"
        }
    }

    feature("getting the contents of a repository") {

        scenario("should get the items of a directory") {
            val exampleResponse = """
                [
                  {
                    "type": "file",
                    "size": 625,
                    "name": "octokit.rb",
                    "path": "lib/octokit.rb",
                    "sha": "fff6fe3a23bf1c8ea0692b4a883af99bee26fd3b",
                    "url": "https://api.github.com/repos/octokit/octokit.rb/contents/lib/octokit.rb",
                    "git_url": "https://api.github.com/repos/octokit/octokit.rb/git/blobs/fff6fe3a23bf1c8ea0692b4a883af99bee26fd3b",
                    "html_url": "https://github.com/octokit/octokit.rb/blob/master/lib/octokit.rb",
                    "download_url": "https://raw.githubusercontent.com/octokit/octokit.rb/master/lib/octokit.rb",
                    "_links": {
                      "self": "https://api.github.com/repos/octokit/octokit.rb/contents/lib/octokit.rb",
                      "git": "https://api.github.com/repos/octokit/octokit.rb/git/blobs/fff6fe3a23bf1c8ea0692b4a883af99bee26fd3b",
                      "html": "https://github.com/octokit/octokit.rb/blob/master/lib/octokit.rb"
                    }
                  },
                  {
                    "type": "dir",
                    "size": 0,
                    "name": "octokit",
                    "path": "lib/octokit",
                    "sha": "a84d88e7554fc1fa21bcbc4efae3c782a70d2b9d",
                    "url": "https://api.github.com/repos/octokit/octokit.rb/contents/lib/octokit",
                    "git_url": "https://api.github.com/repos/octokit/octokit.rb/git/trees/a84d88e7554fc1fa21bcbc4efae3c782a70d2b9d",
                    "html_url": "https://github.com/octokit/octokit.rb/tree/master/lib/octokit",
                    "download_url": null,
                    "_links": {
                      "self": "https://api.github.com/repos/octokit/octokit.rb/contents/lib/octokit",
                      "git": "https://api.github.com/repos/octokit/octokit.rb/git/trees/a84d88e7554fc1fa21bcbc4efae3c782a70d2b9d",
                      "html": "https://github.com/octokit/octokit.rb/tree/master/lib/octokit"
                    }
                  }
                ]
            """.trimIndent()
            val client = GitHubClient("oauth_code_here")
            client.httpClient = getMockedClient(200, exampleResponse)
            val items = client.listDirectory("RegressionGames", "client", "/")
            items shouldHaveSize 2
            items[0].type shouldBe "file"
        }
    }
})
