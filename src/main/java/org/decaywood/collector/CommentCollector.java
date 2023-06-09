package org.decaywood.collector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import org.decaywood.entity.Comment;
import org.decaywood.entity.User;
import org.decaywood.utils.JsonParser;
import org.decaywood.utils.RequestParaBuilder;
import org.decaywood.utils.URLMapper;
import org.springframework.util.CollectionUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author decaywood
 * @date 2020/11/7 12:59
 */
public class CommentCollector extends AbstractCollector<List<Comment>> {

    //帖子ID
    private String postId;

    public CommentCollector(String postId) {
        super(null);
        this.postId = postId;
    }

    //评论的收集逻辑
    @Override
    protected List<Comment> collectLogic() throws Exception {
        //雪球的评论信息JSON路径
        String target = URLMapper.COMMENTS_INFO_JSON.toString();
        //初始化页数，从第一页开始读取
        int page = 1;
        List<Comment> res = new ArrayList<>();
        while (true) {
            //定义评论数20
            int cnt = 20;
            //请求参数构建，初始化评论网址
            RequestParaBuilder builder = new RequestParaBuilder(target)
                    //携带相关请求参数
                    .addParameter("id", postId)
                    .addParameter("count", cnt)
                    .addParameter("page", page)
                    .addParameter("reply", true)
                    .addParameter("split", true);
            URL url = new URL(builder.build());
            //发送请求获取响应json
            String json = request(url);
            JsonNode node = mapper.readTree(json);
            List<Comment> comments = JsonParser.parseArray(Comment::new, (t, n) -> {
                User user = JsonParser.parse(User::new, n.get("user"));
                JsonNode replyComment = n.get("reply_comment");
                if (!(replyComment instanceof NullNode)) {
                    Comment reply_comment = JsonParser.parse(Comment::new, replyComment);
                    User replyUser = JsonParser.parse(User::new, replyComment.get("user"));
                    reply_comment.setUser(replyUser);
                    t.setReply_comment(reply_comment);
                }
                t.setUser(user);
            }, node.get("comments"));
            if (CollectionUtils.isEmpty(comments)) {
                break;
            }
            res.addAll(comments);
            page++;
        }
        return res;
    }
}
