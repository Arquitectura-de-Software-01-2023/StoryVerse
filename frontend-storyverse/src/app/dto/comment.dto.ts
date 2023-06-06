export interface CommentDto {
    commentId: number;
    chapterId: number;
    date: Date;
    description: string;
    status: boolean;
    userId: string;
}